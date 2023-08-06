package com.cozyhome.onlineshop.productservice.repository;

import com.cozyhome.onlineshop.productservice.model.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
    Category getCategoryById(ObjectId id);
}
