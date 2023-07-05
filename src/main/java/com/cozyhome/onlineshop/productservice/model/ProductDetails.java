package com.cozyhome.onlineshop.productservice.model;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;

import com.cozyhome.onlineshop.productservice.model.enums.MaterialEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDetails {

    private float averageRating;

    @Indexed
    private List<MaterialEnum> materials;

    private float weight;

    private float height;

    private float width;

    private float depth;
}
