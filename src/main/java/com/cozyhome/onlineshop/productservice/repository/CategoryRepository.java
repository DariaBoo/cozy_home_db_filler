package com.cozyhome.onlineshop.productservice.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.productservice.model.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {

    List<Category> findAll();
    Optional<Category> findById(ObjectId categoryId);
    Optional<Category> findBySubCategories_Name(String subCategoryName);
}
