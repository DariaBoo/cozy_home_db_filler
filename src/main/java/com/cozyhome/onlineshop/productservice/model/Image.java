package com.cozyhome.onlineshop.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Document(collection = "dataImage")
public class Image {

    @Id
    private ObjectId id;
    private String imagePath;

    public Image(String imagePath) {
        this.imagePath = imagePath;
    }
}
