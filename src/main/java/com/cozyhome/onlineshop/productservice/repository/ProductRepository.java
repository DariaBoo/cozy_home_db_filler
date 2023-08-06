package com.cozyhome.onlineshop.productservice.repository;

import com.cozyhome.onlineshop.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {
    Product getProductBySkuCode(String skuCode);

}
