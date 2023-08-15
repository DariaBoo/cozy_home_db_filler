package com.cozyhome.onlineshop.productservice.fill_database;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataManager {
	private final DataInserter inserter;
	private final DataBuilder builder;

	public void createDataBase() {
		log.info("0 STEP[CREATE DATABASE]");
		inserter.insertColors();
		inserter.insertMaterials();
		inserter.insertCollection();
		builder.buildData();
	}
	
}
