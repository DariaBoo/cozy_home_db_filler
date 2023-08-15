package com.cozyhome.onlineshop.productservice.fill_database;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.productservice.model.ImageProduct;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.repository.ImageProductRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataUpdater {
	private final ProductRepository productRepo;
	private final ImageProductRepository imageProductRepo;
	private final DataBuilder builder;
	private final DataReader reader;

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
		int IMAGE_START_INDEX_COLOR1 = CellIndex.PRODUCT_IMAGE_START_INDEX_COLOR1;
		int IMAGE_START_INDEX_COLOR2 = CellIndex.PRODUCT_IMAGE_START_INDEX_COLOR2;
		int IMAGE_START_INDEX_COLOR3 = CellIndex.PRODUCT_IMAGE_START_INDEX_COLOR3;
		List<ImageProduct> images = imageProductRepo.findAll();
		
	}
	
	public void updateProductDescription() {
		int rowIndex = 2;
		List<Product> products = productRepo.findAll();
		for(Product product : products) {
			String description = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_DESCRIPTION);		
			if(description.isEmpty()) {
				rowIndex++;
				description = reader.readFromExcel(rowIndex, CellIndex.PRODUCT_DESCRIPTION);
			}
			product.setDescription(description);
			productRepo.save(product);
			log.info("PRODUCT WITH SKU[" + product.getSkuCode() + "] IS UPDATED WITH NEW DESCRIPTION");
			rowIndex++;
		}
	}
}
