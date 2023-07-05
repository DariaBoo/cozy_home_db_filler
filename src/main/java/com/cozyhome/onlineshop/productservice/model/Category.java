package com.cozyhome.onlineshop.productservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "testCategory")
public class Category {

    @Id
    private ObjectId id;

    private String name;

    private String description;

    private boolean active;
    
    private List<SubCategory> subCategories = new ArrayList();       
}
