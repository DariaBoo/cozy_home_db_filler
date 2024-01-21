package com.cozyhome.onlineshop.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.model.Inventory;
import com.cozyhome.onlineshop.model.Product;
import com.cozyhome.onlineshop.model.ProductColor;
import com.cozyhome.onlineshop.model.User;
import com.cozyhome.onlineshop.repository.InventoryRepository;
import com.cozyhome.onlineshop.repository.ProductColorRepository;
import com.cozyhome.onlineshop.repository.ProductRepository;
import com.cozyhome.onlineshop.repository.UserRepository;
import com.cozyhome.onlineshop.util.SqlExecuter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostgresManager {
	
	private final SqlExecuter executer;
	private final PostgresBuilder builder;
	private final ProductColorRepository productColorRepo;
	private final InventoryRepository inventoryRepo;
	private final UserRepository userRepo;
	private final ProductRepository productRepo;
	
	private String createDbFile = "/create_db.sql";
//	private String createUserFile = "/create_user.sql";
	private String createTablesFile = "/create_tables2.sql";
	private String createBasketTable = "/create_table_basket_items.sql";
	private String createFavoriteTable = "/create_table_favorite_products.sql";
	private String createReviewTable = "/create_review_table.sql";

	public void createDataBase() {
		log.info("0 STEP[CREATE DATABASE]");
		executer.executeSqlFromFile(createTablesFile);		
		builder.buildProductColorTable();
//		fillInventory();
//		fillFavoriteProducts();
		fillBasket();
	}

	private void fillInventory() {
		List<ProductColor> productColorList = productColorRepo.findAll();
		for (int i = 0; i < productColorList.size(); i++) {
			builder.buildInventory(productColorList.get(i));			
		}
	}
	
	public void fillBasket() {
		List<ProductColor> productColorList = productColorRepo.findAll();
		List<User> users = userRepo.findAll();
		for (ProductColor productColor : productColorList) {
			builder.buildBasket(productColor,users.get(new Random().nextInt(users.size())).getId());			
		}		
	}
	
	public void fillFavoriteProducts() {
		List<Product> products = productRepo.findAll();
		List<User> users = userRepo.findAll();
		for (Product product : products) {
			builder.buildFavoriteProducts(product.getSkuCode(),users.get(new Random().nextInt(users.size())).getId());			
		}		
	}
	
	public void getInventory() {
		List<Inventory> list = inventoryRepo.findAll();
		list.stream().forEach(System.out::println);
	}
}
