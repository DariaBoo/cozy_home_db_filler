package com.cozyhome.onlineshop.productservice.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Document(collection = "dataImage")
public class ImageCategory extends Image {
    @DBRef
    @Indexed
    private Category category;
    private boolean catalog;

    public ImageCategory(ObjectId id, String imagePath, String size, Category category, boolean catalog) {
        super(id, imagePath, size);
        this.category = category;
        this.catalog = catalog;
    }

    public ImageCategory(String imagePath, Category category, boolean catalog) {
        super(imagePath);
        this.category = category;
        this.catalog = catalog;
    }
}
