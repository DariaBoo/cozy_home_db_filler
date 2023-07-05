package com.cozyhome.onlineshop.productservice.repository;

import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {

    List<Product> findAll();
    List<Product> findByStatus(ProductStatus status);
    Product findByName(String name);
    Product findBySkuCode(String skuCode);
    List<Product> findBySubcategoryId(ObjectId id);
}
