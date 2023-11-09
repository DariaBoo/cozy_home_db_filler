package com.cozyhome.onlineshop.fill_database;

import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.service.MongoBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataManager {
	private final DataInserter inserter;
	private final MongoBuilder builder;

	public void createProductDataBase() {
		log.info("0 STEP[CREATE PRODUCT DATABASE]");
		inserter.insertColors();
		inserter.insertMaterials();
		inserter.insertCollection();
		builder.buildData();
	}
	
	public void createUserRoleDataBase() {
		log.info("0 STEP[CREATE USER DATABASE]");
		//inserter.insertRoles();
		inserter.insertUsers();
	}
	
}
