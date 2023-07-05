package com.cozyhome.onlineshop.productservice.service;

import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.Product;

import java.util.List;

import org.bson.types.ObjectId;

public interface CategoryService {

    ObjectId createCategory(Category category);
    
    List<Category> findAllCategories();
    
    Category findByProductName(String productName);
    
    List<Category> findByProductStatus(String productStatus);
}
