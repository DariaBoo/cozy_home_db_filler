package com.cozyhome.onlineshop.productservice.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cozyhome.onlineshop.productservice.model.Role;

public interface RoleRepository extends MongoRepository<Role, ObjectId>{
	
	Role getByRole(String role);

}
