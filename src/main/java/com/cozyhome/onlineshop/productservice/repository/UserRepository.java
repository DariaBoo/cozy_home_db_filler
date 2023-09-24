package com.cozyhome.onlineshop.productservice.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cozyhome.onlineshop.productservice.model.User;

public interface UserRepository extends MongoRepository<User, ObjectId>{

}
