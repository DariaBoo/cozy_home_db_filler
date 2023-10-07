package com.cozyhome.onlineshop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cozyhome.onlineshop.fill_database.DataManager;
import com.cozyhome.onlineshop.fill_database.DataUpdater;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class DataBaseFillerApplication {

    private final DataManager dataManager;
    private final DataUpdater dataUpdater;

    public static void main(String[] args) {
		SpringApplication.run(DataBaseFillerApplication.class, args);
	}

    @Bean
    public CommandLineRunner loadData() {
      return args -> {
    	  dataManager.createUserRoleDataBase();
//    	  dataManager.createProductDataBase();
//          dataUpdater.updateProductAvailable();
//    	  dataUpdater.updateProductDescription();
    	  //dataUpdater.updateProductMeasurements();
      };
  }
}
