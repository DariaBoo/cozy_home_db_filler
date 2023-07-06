package com.cozyhome.onlineshop.productservice.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.productservice.model.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {

}
