package com.cozyhome.onlineshop.productservice.controller;

import com.cozyhome.onlineshop.productservice.dto.ProductRequestDto;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class ProductController {

    private final ProductService service;

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody Product product) {//@RequestParam String subcategoryName, 
        service.createProduct(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getBySubcategoryName(@RequestParam String subcategoryName) {
        return new ResponseEntity<>(service.getProductsBySubcategoryName(subcategoryName), HttpStatus.OK);
    }
    
    @GetMapping("/products/status")
    public ResponseEntity<List<Product>> getByStatus(@RequestParam String productStatus) {
        return new ResponseEntity<>(service.getProductsByStatus(productStatus), HttpStatus.OK);
    }
    
    @GetMapping("/products/category&status")
    public ResponseEntity<List<Product>> getByStatusForCategory(@RequestParam String categoryName, @RequestParam String productStatus) {
        return new ResponseEntity<>(service.getProductsByStatusForCategory(categoryName, productStatus), HttpStatus.OK);
    }
    
    @GetMapping("/products/sku")
    public ResponseEntity<Product> getBySku(@RequestParam String sku) {
        return new ResponseEntity<>(service.getByProductSku(sku), HttpStatus.OK);
    }   
    
    @GetMapping("/products/name")
    public ResponseEntity<Product> getByName(@RequestParam String name) {
        return new ResponseEntity<>(service.getProductsByName(name), HttpStatus.OK);
    }
    
    @GetMapping("/products/material")
    public ResponseEntity<List<Product>> getByMaterial(@RequestParam String material) {
        return new ResponseEntity<>(service.getByProductMaterial(material), HttpStatus.OK);
    }
    
    @GetMapping("/products_all")
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    } 
    
    @GetMapping("/products/subcategory")
    public ResponseEntity<List<Product>> getBySubcategoryNameFromId(@RequestParam String name) {
        return new ResponseEntity<>(service.getBySubcategoryName(name), HttpStatus.OK);
    } 
}
