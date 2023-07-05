package com.cozyhome.onlineshop.productservice.service.impl;

import com.cozyhome.onlineshop.productservice.model.Category;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.SubCategory;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.ProductRepository;
import com.cozyhome.onlineshop.productservice.repository.SubcategoryRepository;
import com.cozyhome.onlineshop.productservice.service.CategoryService;
import com.cozyhome.onlineshop.productservice.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    private final CategoryRepository repository;
    private final SubcategoryRepository subRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void createSubCategory(ObjectId categoryId, SubCategory subCategory) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        List<SubCategory> subcategories = category.getSubCategories();
//        if (subcategories == null) {
//            subcategories = new ArrayList<>();
//        }
        subcategories.add(subCategory);
        category.setSubCategories(subcategories);
        repository.save(category);
    }

    @Override
    public SubCategory findBySubcategoryName(String name) {
//        return repository.findBySubCategories_Name(name);
        Aggregation aggregation = newAggregation(match(Criteria.where("subCategories.name").is(name)),
                unwind("subCategories"), replaceRoot("subCategories"));

        SubCategory result = mongoTemplate
                .aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), SubCategory.class)
                .getUniqueMappedResult();
        return result;
    }

    @Override
    public List<SubCategory> findAllSubCategories() {
    Aggregation aggregation = newAggregation(
                Aggregation.unwind("subCategories"),            
                Aggregation.replaceRoot("subCategories")
        );

        return mongoTemplate.aggregate(aggregation, "category", SubCategory.class).getMappedResults();
    }
}
