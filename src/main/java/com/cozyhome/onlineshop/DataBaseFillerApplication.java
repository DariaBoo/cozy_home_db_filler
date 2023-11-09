package com.cozyhome.onlineshop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cozyhome.onlineshop.service.MongoManager;
import com.cozyhome.onlineshop.service.DataUpdater;
import com.cozyhome.onlineshop.service.PostgresManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EntityScan(basePackages = {"com.cozyhome.onlineshop.model"})
@EnableJpaRepositories(basePackages = {"com.cozyhome.onlineshop.repository"})
@SpringBootApplication
public class DataBaseFillerApplication {

    private final MongoManager dataManager;
    private final DataUpdater dataUpdater;
    private final PostgresManager manager;

    
	public static void main(String[] args) {
		SpringApplication.run(DataBaseFillerApplication.class, args);
	}
	
	 @Bean
  public CommandLineRunner loadData() {
      return args -> {
    	  dataManager.createUserRoleDataBase();
//	      manager.createDataBase();
//    	  dataManager.createProductDataBase();
//          dataUpdater.updateProductAvailable();
//    	  dataUpdater.updateProductDescription();
    	  //dataUpdater.updateProductMeasurements();
      };
  }	
}
