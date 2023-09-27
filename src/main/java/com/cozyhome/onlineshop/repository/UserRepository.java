package com.cozyhome.onlineshop.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cozyhome.onlineshop.model.User;

public interface UserRepository extends MongoRepository<User, ObjectId>{

}
