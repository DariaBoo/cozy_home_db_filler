package com.cozyhome.onlineshop.productservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.cozyhome.onlineshop.productservice.model.Color;
import com.cozyhome.onlineshop.productservice.model.Discount;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.ProductCollection;
import com.cozyhome.onlineshop.productservice.model.ProductDetails;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class ProductRequestDto {

    private ObjectId id;

    private String name;

    //@Indexed(unique = true)
    private String skuCode;

    private String description;

    private BigDecimal price;

    private ProductStatus status;

    private final ProductDetails productDetails;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

//    @DBRef
//    private Category subCategory;

//    @DBRef
    private Discount discount;

//    @DBRef
    private ProductCollection productCollection;

    private List<Color> colors;
}
