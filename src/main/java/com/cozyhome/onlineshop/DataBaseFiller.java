package com.cozyhome.onlineshop;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cozyhome.onlineshop.service.MongoManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class DataBaseFiller {
	
	private final MongoManager dataManager;

}
