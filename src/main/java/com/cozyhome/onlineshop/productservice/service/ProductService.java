package com.cozyhome.onlineshop.productservice.service;

import com.cozyhome.onlineshop.productservice.model.Product;

import java.util.List;

import org.bson.types.ObjectId;

public interface ProductService {

    List<Product> findAllProducts();
    
    void createProduct(Product product);
    
    List<Product> getProductsBySubcategoryName(String subCategoryName);
    
    List<Product> getProductsByStatus(String productStatus);
    Product getByProductSku(String sku);
    
    List<Product> getProductsByStatusForCategory(String categoryName, String productStatus);
    
    List<Product> getByProductMaterial(String material);
    List<Product> getAllProducts();
    Product getProductsByName(String productName);
    
    List<Product> getBySubcategoryName(String subCategoryName);
}
