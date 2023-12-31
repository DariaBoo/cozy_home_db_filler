package com.cozyhome.onlineshop.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.model.Color;

@Repository
public interface ColorRepository extends MongoRepository<Color, ObjectId> {

	Color getByName(String name);
	
}
