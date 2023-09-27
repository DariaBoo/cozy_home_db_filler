package com.cozyhome.onlineshop.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.model.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
    Category getCategoryById(ObjectId id);
}
