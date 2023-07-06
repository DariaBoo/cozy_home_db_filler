package com.cozyhome.onlineshop.productservice.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "dataCategory")
public class Category {

    @Id
    private ObjectId id;

    private String name;

    private boolean active;
    
    private ObjectId parentId;       
}
