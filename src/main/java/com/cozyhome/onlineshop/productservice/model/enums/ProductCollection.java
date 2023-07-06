package com.cozyhome.onlineshop.productservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCollection {

    FUTURE("future"),
    TENDERNESS("tenderness"),
    FREEDOM("freedom"),
    SOFT("soft"),
    KASPER("kasper"),
    BUSINESS("business"),
    THINK("think");

    private final String description;

    public static ProductCollection getCollectionByDescription(String description) {
        for (ProductCollection collection : ProductCollection.values()){
            if(collection.getDescription().equals(description)) {
                return collection;
            }
        }
        throw new IllegalArgumentException("Invalid product collection description: " + description);
    }
    
}
