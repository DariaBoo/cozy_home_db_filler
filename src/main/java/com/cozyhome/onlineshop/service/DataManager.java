package com.cozyhome.onlineshop.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataManager {
	private final DataInserter inserter;
	private final MongoBuilder builder;
	private final DataUpdater updater;

	public void updateProductDataBase() {
		log.info("0 STEP[CREATE PRODUCT DATABASE]");
		updater.updateProductName();
		updater.updateProductShortDescription();
		updater.updateProductDescription();
	}

	public void createProductDataBase() {
		log.info("0 STEP[CREATE PRODUCT DATABASE]");
		inserter.insertColors();
		inserter.insertMaterials();
		inserter.insertCollection();
		builder.buildData();
	}

	public void createUserRoleDataBase() {
		log.info("0 STEP[CREATE USER DATABASE]");
		// inserter.insertRoles();
		inserter.insertUsers();
	}

}
