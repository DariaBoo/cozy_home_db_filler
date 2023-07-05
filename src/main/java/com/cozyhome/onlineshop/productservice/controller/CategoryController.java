package com.cozyhome.onlineshop.productservice.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.SubCategory;
import com.cozyhome.onlineshop.productservice.service.CategoryService;
import com.cozyhome.onlineshop.productservice.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class CategoryController {

    private final CategoryService service;
    private final SubCategoryService subCategoryService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody Category category) {
        service.createCategory(category);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAll() {
        return new ResponseEntity<>(service.findAllCategories(), HttpStatus.OK);
    }
    
    @PostMapping("/subcategories")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSubCategory(@RequestParam ObjectId categoryId, @RequestBody SubCategory subCategory) {
        subCategoryService.createSubCategory(categoryId, subCategory);
    }
    
    @GetMapping("/subcategories")
    public ResponseEntity<SubCategory> findSubcategoryByName(@RequestParam String name) {
        return new ResponseEntity<>(subCategoryService.findBySubcategoryName(name), HttpStatus.OK);
    }
    
    @GetMapping("/category")
    public ResponseEntity<Category> findByProductName(@RequestParam String productName) {
        return new ResponseEntity<>(service.findByProductName(productName), HttpStatus.OK);
    }
    
    @GetMapping("/categories_status")
    public ResponseEntity<List<Category>> findByProductStatus(@RequestParam String productStatus) {
        return new ResponseEntity<>(service.findByProductStatus(productStatus), HttpStatus.OK);
    }
    
    @GetMapping("/subcategories_all")
    public ResponseEntity<List<SubCategory>> getAllSubcategories() {
        return new ResponseEntity<>(subCategoryService.findAllSubCategories(), HttpStatus.OK);
    }
}
