package com.cozyhome.onlineshop.productservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cozyhome.onlineshop.productservice.fill_database.DataCreator;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class ProductServiceApplication {

    private final DataCreator creator;
    
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
//	@Bean
//	public CommandLineRunner loadData(CategoryRepository categoryRepository, DataCreator dataCreator) {
//	    return args -> {
//	        creator.createCategories(4, 5, 40000);
//	    };
//	}
	
}
