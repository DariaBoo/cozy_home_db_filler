package com.cozyhome.onlineshop.productservice.repository;

import com.cozyhome.onlineshop.productservice.model.ImageCategory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageCategoryRepository extends MongoRepository<ImageCategory, ObjectId> {
}
