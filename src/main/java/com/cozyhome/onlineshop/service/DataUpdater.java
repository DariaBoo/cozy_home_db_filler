package com.cozyhome.onlineshop.service;

import static com.cozyhome.onlineshop.util.CellIndex.PRODUCT_QUANTITY_FOR_COLOR_17;
import static com.cozyhome.onlineshop.util.CellIndex.PRODUCT_QUANTITY_FOR_COLOR_38;
import static com.cozyhome.onlineshop.util.CellIndex.PRODUCT_QUANTITY_FOR_COLOR_59;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalDouble;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.dto.ProductMeasurementsDto;
import com.cozyhome.onlineshop.model.Inventory;
import com.cozyhome.onlineshop.model.Product;
import com.cozyhome.onlineshop.model.Review;
import com.cozyhome.onlineshop.repository.InventoryRepository;
import com.cozyhome.onlineshop.repository.ProductRepository;
import com.cozyhome.onlineshop.repository.ReviewRepository;
import com.cozyhome.onlineshop.util.CellIndex;
import com.cozyhome.onlineshop.util.DataReader;
import com.cozyhome.onlineshop.util.RowIndex;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataUpdater {
	private final ProductRepository productRepo;
	private final MongoBuilder builder;
	private final DataReader reader;
	private final ReviewRepository reviewRepo;
	private final InventoryRepository inventoryRepo;
	private final Map<Integer, Integer> colorsQuantityConstants = new HashMap<>();
	
	private final int SKUCODE_COLUMN_INDEX = 0;

	{
		colorsQuantityConstants.put(0, PRODUCT_QUANTITY_FOR_COLOR_17);
		colorsQuantityConstants.put(1, PRODUCT_QUANTITY_FOR_COLOR_38);
		colorsQuantityConstants.put(2, PRODUCT_QUANTITY_FOR_COLOR_59);
	}
	
	public void setNewProductsQuantity() {
		List<Product> newProducts = productRepo.findAllByStatus("NEW");
		newProducts.stream().map(Product::getSkuCode).forEach(System.out::println);
	}

	public void updatePriceWithDiscount() {
		log.info("STEP 1[UPDATE PRICE WITH DISCOUNT]");
		List<Product> products = productRepo.findAll();
		for (Product product : products) {
			BigDecimal price = product.getPrice();
			String discount = String.valueOf(product.getDiscount());
			product.setPriceWithDiscount(builder.calculatePriceWithDiscount(price, discount));
			productRepo.save(product);
			log.info("PRODUCT WITH SKU[" + product.getSkuCode() + "] IS UPDATED WITH PRICE_WITH_DISCOUNT["
					+ product.getPriceWithDiscount() + "].");
		}
	}

	public void updateImageProduct() {
		int rowIndex = RowIndex.FIRST_INDEX;
		int lastRowIndex = RowIndex.LAST_INDEX;
		while (rowIndex <= lastRowIndex) {
			String productSkuCode = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_SKU);
			if (!productSkuCode.isEmpty()) {
				log.info("CREATING IMAGES FOR PRODUCT WITH SKUCODE[" + productSkuCode + "]...");
				builder.buildImages(rowIndex, productSkuCode);
			}
			rowIndex++;
		}
	}

	public void updateProductAvailable() {
		List<Product> products = productRepo.findAll();

		for (Product product : products) {
			boolean available = false;
			int count = 0;
			int rowIndex = reader.findRowIndexByValue(product.getSkuCode(), CellIndex.PRODUCT_SKU);

			for (int i = 0; i < 3; i++) {
				String quantityString = reader.readFromExcel(rowIndex, colorsQuantityConstants.get(i)).trim();
				int quantity;

				if (quantityString.isEmpty()) {
					quantity = 0;
				} else {
					quantity = Math.round(Float.parseFloat(quantityString));
				}
				count = count + quantity;
			}

			if (count > 0) {
				available = true;
			}


			product.setAvailable(available);
			productRepo.save(product);
			log.info("PRODUCT WITH SKU[" + product.getSkuCode() + "] IS UPDATED WITH NEW AVAILABLE: " + available);
		}
	}

	public void updateProductDescription() {
		List<Product> products = productRepo.findAll();
		for (Product product : products) {
			int rowIndex = reader.findRowIndexByValue(product.getSkuCode(), SKUCODE_COLUMN_INDEX);
			String description = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_DESCRIPTION);
			if (!description.isEmpty()) {
				product.setDescription(description);
			}
			productRepo.save(product);
			log.info("PRODUCT WITH SKU[" + product.getSkuCode() + "] IS UPDATED WITH NEW DESCRIPTION");
		}
	}
	public void updateProductName() {
		List<Product> products = productRepo.findAll();
		for (Product product : products) {
			int rowIndex = reader.findRowIndexByValue(product.getSkuCode(), SKUCODE_COLUMN_INDEX);
			String name = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_NAME);
			if (!name.isEmpty()) {
				product.setName(name);
			}
			productRepo.save(product);
			log.info("PRODUCT WITH SKU[" + product.getSkuCode() + "] IS UPDATED WITH NEW NAME");
		}
	}
	public void updateProductShortDescription() {
		List<Product> products = productRepo.findAll();
		for (Product product : products) {
			int rowIndex = reader.findRowIndexByValue(product.getSkuCode(), SKUCODE_COLUMN_INDEX);
			String description = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_SHORT_DESCRIPTION);
			if (!description.isEmpty()) {
				product.setShortDescription(description);
			}
			productRepo.save(product);
			log.info("PRODUCT WITH SKU[" + product.getSkuCode() + "] IS UPDATED WITH NEW SHORT DESCRIPTION");
		}
	}

	public void updateProductMeasurements() {		
		List<Product> products = productRepo.findAll();
		for (Product product : products) {
			int rowIndex = reader.findRowIndexByValue(product.getSkuCode(), SKUCODE_COLUMN_INDEX);
			ProductMeasurementsDto dto = reader.readProductMeasurements(rowIndex);
			if (!dto.getWeight().isEmpty()) {
				product.setWeight(convertToFormattedFloat(dto.getWeight()));
			}
			if(!dto.getHeight().isEmpty()) {
				product.setHeight(convertToFormattedFloat(dto.getHeight()));
			}
			if(!dto.getWidth().isEmpty()) {
				product.setWidth(convertToFormattedFloat(dto.getWidth()));
			}
			if(!dto.getDepth().isEmpty()) {
				product.setDepth(convertToFormattedFloat(dto.getDepth()));
			}
			if (!dto.getBedLength().isEmpty()) {
				product.setBedLength(convertToFormattedFloat(dto.getBedLength()));
			}
			if (!dto.getBedWidth().isEmpty()) {
				product.setBedWidth(convertToFormattedFloat(dto.getBedWidth()));
			}
			productRepo.save(product);
			log.info("PRODUCT WITH SKU[" + product.getSkuCode() + "] IS UPDATED WITH NEW MEASUREMENTS");
		}
	}

	private Float convertToFormattedFloat(String value) {
		Float floatValue = Float.parseFloat(value);
        floatValue = Math.round(floatValue * 10.0f) / 10.0f;
        return floatValue;
	}
	
	public void updateProductAvailability() {
		List<Product> products = productRepo.findAll();
		for(Product product : products) {
			int quantity = inventoryRepo.findByProductColorProductSkuCode(product.getSkuCode()).stream().mapToInt(Inventory::getQuantity).sum();
			if(quantity > 0) {
				product.setAvailable(true);
			} else {
				product.setAvailable(false);
			}
			log.info("SET AVAILABILITY [" + product.getAvailable() + "] FOR PRODUCT ["  + product.getSkuCode() + "] WITH QUANTITY [" + quantity + "].");
			productRepo.save(product);
		}
	}
	
	public void updateProductAverageRating() {
		List<Product> products = productRepo.findAll();
		 DecimalFormat decimalFormat = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.US));

		for(Product product : products) {
			OptionalDouble averageRating = reviewRepo.findReviewsByProductSkuCode(product.getSkuCode()).stream().mapToInt(Review::getRating).average();
			
			if(averageRating.isPresent()) {
				float formattedRating = Float.parseFloat(decimalFormat.format(averageRating.getAsDouble()));
	            product.setAverageRating(formattedRating);
			} else {
				product.setAverageRating(null);
			}
			log.info("SET AVERAGE RATING [" + product.getAverageRating() + "] FOR PRODUCT ["  + product.getSkuCode() + "].");
			productRepo.save(product);
		}
	}
}
