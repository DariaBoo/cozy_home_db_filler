package com.cozyhome.onlineshop.productservice.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "dataImage")
public class Image {

    @Id
    private ObjectId id;
    private String imagePath;    
    private  boolean preview;    
    @DBRef
    private Color color;  
    @Indexed
    private String productSkuCode;

}
