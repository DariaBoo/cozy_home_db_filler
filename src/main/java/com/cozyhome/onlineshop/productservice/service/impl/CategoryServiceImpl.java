package com.cozyhome.onlineshop.productservice.service.impl;

import com.cozyhome.onlineshop.productservice.model.Category;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;
import com.cozyhome.onlineshop.productservice.service.CategoryService;

import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final MongoTemplate mongoTemplate;
    
    @Override
    public ObjectId createCategory(Category category) {
        Category savedCategory = repository.save(category);
        return savedCategory.getId();
    }
    
    @Override
    public List<Category> findAllCategories() {
        return repository.findAll();
    }

    @Override
    public Category findByProductName(String productName) {   
        Aggregation aggregation = newAggregation(match(Criteria.where("subCategories.products.name").is(productName)));
          Category result = mongoTemplate
                  .aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), Category.class)
                  .getUniqueMappedResult();
          return result;
//        return repository.findBySubCategoriesProducts_Name(productName).orElseThrow(() -> new IllegalArgumentException("No category found"));
    }
    
    @Override
    public List<Category> findByProductStatus(String productStatus) {
    Aggregation aggregation = newAggregation(
          match(Criteria.where("subCategories.products.status").is(ProductStatus.getStatusByDescription(productStatus))),
          unwind("subCategories"),
          lookup("category", "subCategories.categoryId", "_id", "subCategories.category"),
          unwind("category"),
          unwind("subCategories.products"),
          match(Criteria.where("subCategories.products.status").is(ProductStatus.getStatusByDescription(productStatus)))
      );
    return mongoTemplate
            .aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), Category.class)
            .getMappedResults();
    }
}
