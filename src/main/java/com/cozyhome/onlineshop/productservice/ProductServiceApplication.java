package com.cozyhome.onlineshop.productservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cozyhome.onlineshop.productservice.fill_database.DataCreator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class ProductServiceApplication {

    private final DataCreator dataCreator;
    
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
	 @Bean
  public CommandLineRunner loadData() {
      return args -> {
          dataCreator.createCategories();
      };
  }	
}
