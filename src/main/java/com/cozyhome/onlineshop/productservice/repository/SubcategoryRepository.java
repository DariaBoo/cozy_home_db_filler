package com.cozyhome.onlineshop.productservice.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.productservice.model.SubCategory;

@Repository
public interface SubcategoryRepository extends MongoRepository<SubCategory, ObjectId> {

    List<SubCategory> findAll();
}
