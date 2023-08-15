package com.cozyhome.onlineshop.productservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cozyhome.onlineshop.productservice.fill_database.DataBuilder;
import com.cozyhome.onlineshop.productservice.fill_database.ProductUpdater;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class ProductServiceApplication {

    private final DataBuilder dataBuilder;
    private final ProductUpdater productUpdater;
    
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
	 @Bean
  public CommandLineRunner loadData() {
      return args -> {
//          dataBuilder.insertData();
    	  productUpdater.buildPriceWithDiscount();
      };
  }	
}
