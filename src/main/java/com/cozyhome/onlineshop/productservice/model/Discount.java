package com.cozyhome.onlineshop.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document
public class Discount {

    @Id
    private ObjectId id;

    private String name;

    private String description;

    private byte discountPercent;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
