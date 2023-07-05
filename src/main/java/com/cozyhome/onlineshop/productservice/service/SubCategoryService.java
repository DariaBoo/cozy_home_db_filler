package com.cozyhome.onlineshop.productservice.service;

import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.SubCategory;

import java.util.List;

import org.bson.types.ObjectId;

public interface SubCategoryService {

    void createSubCategory(ObjectId categoryId, SubCategory subCategory);
    
    List<SubCategory> findAllSubCategories();
    SubCategory findBySubcategoryName(String name);
    
}
