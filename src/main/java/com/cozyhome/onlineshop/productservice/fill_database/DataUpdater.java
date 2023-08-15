package com.cozyhome.onlineshop.productservice.fill_database;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.productservice.dto.ProductMeasurementsDto;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataUpdater {
	private final ProductRepository productRepo;
	private final DataBuilder builder;
	private final DataReader reader;
	
	private final int SKUCODE_COLUMN_INDEX = 0;

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
}
