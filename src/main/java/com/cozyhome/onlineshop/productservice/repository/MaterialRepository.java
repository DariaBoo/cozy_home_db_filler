package com.cozyhome.onlineshop.productservice.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.productservice.model.Material;

@Repository
public interface MaterialRepository extends MongoRepository<Material, ObjectId> {

	Material getByName(String name);
	
}
