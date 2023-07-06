package com.cozyhome.onlineshop.productservice.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.productservice.model.Image;

@Repository
public interface ImageRepository extends MongoRepository<Image, ObjectId> {

    Optional<Image> findByProductId(ObjectId productId);

}
