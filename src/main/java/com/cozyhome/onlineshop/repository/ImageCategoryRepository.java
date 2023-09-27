package com.cozyhome.onlineshop.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.model.ImageCategory;

@Repository
public interface ImageCategoryRepository extends MongoRepository<ImageCategory, ObjectId> {
}
