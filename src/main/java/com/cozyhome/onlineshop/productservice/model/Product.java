package com.cozyhome.onlineshop.productservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cozyhome.onlineshop.productservice.model.enums.ProductCollection;
import com.cozyhome.onlineshop.productservice.model.enums.ProductMaterial;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
@Document(collection = "dataProduct")
public class Product {

    @Indexed(unique = true)
    private String skuCode;
    @Indexed
    private String name;
    private String description;
    private String shortDescription;
    @Indexed
    private BigDecimal price;
    private byte discount;
    @Indexed
    private ProductStatus status;
    private ProductCollection productCollection;
    @Indexed
    private ObjectId categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;    
    @Indexed
    private float averageRating;
    @Indexed
    private List<ProductMaterial> materials;
    private float weight;
    private float height;
    private float width;
    private float depth;
       
}
