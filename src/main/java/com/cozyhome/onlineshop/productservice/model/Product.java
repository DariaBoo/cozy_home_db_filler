package com.cozyhome.onlineshop.productservice.model;

import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
@Document(collection = "testProduct")
public class Product {

    @Indexed
    private String skuCode;

    @Indexed
    private String name;

    private String shortDescription;
    private String description;

    private BigDecimal price;

    @Indexed
    private ProductStatus status;

    private ProductDetails productdetails;

    private List<Color> colors = new ArrayList();
    
    private Byte discountPercent;
    
    private String productCollection;
    
    @Indexed
    private ObjectId subcategoryId;
    
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;        
}
