package com.cozyhome.onlineshop.productservice.model;


import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubCategory {

    @Id
    private ObjectId id;

    private String name;

    private String description;

    private boolean active; 
    
//    private List<Product> products = new ArrayList();
}
