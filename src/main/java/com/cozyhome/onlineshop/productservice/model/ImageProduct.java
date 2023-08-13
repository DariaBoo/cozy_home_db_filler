package com.cozyhome.onlineshop.productservice.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Document(collection = "dataImage")
public class ImageProduct extends Image {

    private boolean preview;
    @DBRef
    private Color color;
    @DBRef
    @Indexed
    private Product product;

    public ImageProduct(ObjectId id, String imagePath, String size, boolean preview, Color color, Product product) {
        super(id, imagePath, size);
        this.preview = preview;
        this.color = color;
        this.product = product;
    }

    public ImageProduct(String imagePath, boolean preview, Color color, Product product) {
        super(imagePath);
        this.preview = preview;
        this.color = color;
        this.product = product;
    }
}
