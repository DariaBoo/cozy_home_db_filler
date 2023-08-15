package com.cozyhome.onlineshop.productservice.fill_database;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductUpdater {
	private final ProductRepository productRepo;
	private final DataBuilder dataBuilder;

	public void buildPriceWithDiscount() {
		log.info("STEP 1");
		List<Product> products = productRepo.findAll();
		for (Product product : products) {
			BigDecimal price = product.getPrice();
			String discount = String.valueOf(product.getDiscount());
			product.setPriceWithDiscount(dataBuilder.calculatePriceWithDiscount(price, discount));
			productRepo.save(product);
			log.info("PRODUCT WITH SKU[" + product.getSkuCode() + "] is updated with priceWithDiscount["
					+ product.getPriceWithDiscount() + "].");
		}
	}
}
