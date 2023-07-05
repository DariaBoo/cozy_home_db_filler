package com.cozyhome.onlineshop.productservice.model;

import com.cozyhome.onlineshop.productservice.model.enums.ColorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Color {

    @Id
    private ObjectId id;

    private ColorEnum colorEnum;

    private List<ProductPhoto> photos;

}
