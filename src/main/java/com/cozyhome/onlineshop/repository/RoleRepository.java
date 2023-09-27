package com.cozyhome.onlineshop.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cozyhome.onlineshop.model.Role;

public interface RoleRepository extends MongoRepository<Role, ObjectId>{
	
	Role getByName(String name);

}
