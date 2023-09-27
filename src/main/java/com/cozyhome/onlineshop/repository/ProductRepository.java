package com.cozyhome.onlineshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {
    Product getProductBySkuCode(String skuCode);

}
