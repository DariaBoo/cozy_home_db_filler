package com.cozyhome.onlineshop.productservice.service.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.SubCategory;
import com.cozyhome.onlineshop.productservice.model.enums.MaterialEnum;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;
import com.cozyhome.onlineshop.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Product> findAllProducts() {
        return repository.findAll();
    }

    @Override
    public void createProduct(Product product) {
        repository.save(product);
    }

    @Override
    public List<Product> getProductsBySubcategoryName(String subCategoryName) {// TODO add exceptions, correct the body
        Aggregation aggregation = newAggregation(match(Criteria.where("subCategories.name").is(subCategoryName)),
                unwind("subCategories"), replaceRoot("subCategories"), unwind("products"), replaceRoot("products"));

        List<Product> result = mongoTemplate
                .aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), Product.class)
                .getMappedResults();
        return result;
    }

    @Override
    public Product getByProductSku(String sku) {
        Aggregation aggregation = newAggregation(match(Criteria.where("subCategories.products.skuCode").is(sku)),
                unwind("subCategories"), replaceRoot("subCategories"), unwind("products"),
                match(Criteria.where("products.skuCode").is(sku)), replaceRoot("products"));
        Product result = mongoTemplate
                .aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), Product.class)
                .getUniqueMappedResult();
        return repository.findBySkuCode(sku);
    }

    @Override
    public List<Product> getAllProducts() {
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind("subCategories"),
                Aggregation.unwind("subCategories.products"), Aggregation.replaceRoot("subCategories.products"));
        AggregationResults<Product> results = mongoTemplate.aggregate(aggregation, "category", Product.class);
        return repository.findAll();
    }

    @Override
    public List<Product> getProductsByStatus(String productStatus) {
//        Aggregation aggregation = newAggregation(
//                match(Criteria.where("subCategories.products.status")
//                        .is(ProductStatus.getStatusByDescription(productStatus))),
//                unwind("subCategories"), replaceRoot("subCategories"), unwind("products"), replaceRoot("products"));
//
//        List<Product> result = mongoTemplate
//                .aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), Product.class)
//                .getMappedResults();
        return repository.findByStatus(ProductStatus.getStatusByDescription(productStatus));
    }

    @Override
    public Product getProductsByName(String productName) {
        Aggregation aggregation = newAggregation(match(Criteria.where("subCategories.products.name").is(productName)),
                unwind("subCategories"), replaceRoot("subCategories"), unwind("products"), replaceRoot("products"));

        Product result = mongoTemplate
                .aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), Product.class)
                .getUniqueMappedResult();
        return repository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByStatusForCategory(String categoryName, String productStatus) {
        Aggregation aggregation = newAggregation(match(Criteria.where("name").is(categoryName)),
                unwind("subCategories"), unwind("subCategories.products"),
                match(Criteria.where("subCategories.products.status")
                        .is(ProductStatus.getStatusByDescription(productStatus))),
                replaceRoot("subCategories.products"));

        return mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), Product.class)
                .getMappedResults();
    }

    @Override
    public List<Product> getByProductMaterial(String material) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("productdetails.materials").in(MaterialEnum.getByDescription(material))),
                Aggregation.unwind("productdetails"),
                Aggregation.match(Criteria.where("productdetails.materials").in(MaterialEnum.getByDescription(material)))
        );

        return mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Product.class), Product.class)
                .getMappedResults();
    }
    
    @Override
    public List<Product> getBySubcategoryName(String subCategoryName){
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("subCategories.name").is(subCategoryName)),
                Aggregation.unwind("subCategories"),
                Aggregation.match(Criteria.where("subCategories.name").is(subCategoryName)),
                replaceRoot("subCategories")
        );
        SubCategory sub = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), SubCategory.class).getUniqueMappedResult();

        return repository.findBySubcategoryId(sub.getId());
        
    }
}
