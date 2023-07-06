package com.cozyhome.onlineshop.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cozyhome.onlineshop.productservice.model.enums.ProductColor;

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
    private ProductColor color;    
    private String productId;

}
