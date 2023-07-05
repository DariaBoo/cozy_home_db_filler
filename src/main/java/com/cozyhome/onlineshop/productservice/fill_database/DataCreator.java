package com.cozyhome.onlineshop.productservice.fill_database;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.Color;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.model.ProductDetails;
import com.cozyhome.onlineshop.productservice.model.ProductPhoto;
import com.cozyhome.onlineshop.productservice.model.SubCategory;
import com.cozyhome.onlineshop.productservice.model.enums.ColorEnum;
import com.cozyhome.onlineshop.productservice.model.enums.MaterialEnum;
import com.cozyhome.onlineshop.productservice.model.enums.ProductStatus;
import com.cozyhome.onlineshop.productservice.service.CategoryService;
import com.cozyhome.onlineshop.productservice.service.ProductService;
import com.cozyhome.onlineshop.productservice.service.SubCategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DataCreator {

    private final CategoryService categoryService;
    private final SubCategoryService subcategoryService;
    private final ProductService productService;
    
    public void createCategories(int numCategories, int numSubcategoriesInCategory, int numProducts) {
        Random random = new Random();
        
        for (int z = 0; z < numCategories; z++) {
            Category category = new Category();
            category.setName("Category" + z);
            category.setDescription("Description " + random.nextInt(20000));
            category.setActive(true);
            ObjectId categoryId = categoryService.createCategory(category);
            System.out.println("Category is created");
            for (int y = 0; y < numSubcategoriesInCategory; y++) {
                SubCategory subcategory = new SubCategory();
                subcategory.setId(new ObjectId());
                subcategory.setName("subcategory " + z + "." + y);
                subcategory.setDescription("description " + z + "." + y);
                subcategory.setActive(true);
                
                int numProductsInSubcategory = numProducts / numCategories / numSubcategoriesInCategory;
                
                subcategoryService.createSubCategory(categoryId, subcategory);
                
                ObjectId subcategoryId = subcategory.getId();
                for (int i = 0; i < numProductsInSubcategory; i++) {
                    Color color1 = Color.builder().colorEnum(ColorEnum.values()[random.nextInt(2)])
                            .photos(Stream.of(ProductPhoto.builder().path("path").build()).collect(Collectors.toList()))
                            .build();
                    Color color2 = Color.builder().colorEnum(ColorEnum.values()[random.nextInt(3)])
                            .photos(Stream.of(ProductPhoto.builder().path("path").build()).collect(Collectors.toList()))
                            .build();
                    List<Color> colors = Stream.of(color1, color2).collect(Collectors.toList());
                    
                    ProductDetails details = ProductDetails.builder().averageRating(random.nextInt(6))
                            .materials(Stream.of(MaterialEnum.values()[random.nextInt(3)]).collect(Collectors.toList()))
                            .weight(random.nextInt(1000)).height(random.nextInt(1000)).width(random.nextInt(1000))
                            .depth(random.nextInt(1000)).build();

                    Product product = Product.builder().skuCode("SKU " + z + '.' + y + '.' + i).name("Product " + z + '.' + y + '.' + i)
                            .shortDescription("Short description " + random.nextInt(20000))
                            .description("Full description " + random.nextInt(20000))
                            .price(BigDecimal.valueOf(random.nextInt(20000)))
                            .status(ProductStatus.values()[random.nextInt(4)]).productdetails(details).colors(colors)
                            .discountPercent((byte) random.nextInt(100))
                            .productCollection("Collection " + random.nextInt(20)).createdAt(LocalDateTime.now())
                            .subcategoryId(subcategoryId)
                            .build();
                    productService.createProduct(product);
                }
            }
        }
    }

}
