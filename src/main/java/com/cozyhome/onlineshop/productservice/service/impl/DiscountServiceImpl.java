package com.cozyhome.onlineshop.productservice.service.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.productservice.model.Category;
import com.cozyhome.onlineshop.productservice.model.Discount;
import com.cozyhome.onlineshop.productservice.model.Product;
import com.cozyhome.onlineshop.productservice.repository.CategoryRepository;
import com.cozyhome.onlineshop.productservice.repository.DiscountRepository;
import com.cozyhome.onlineshop.productservice.service.DiscountService;
import com.cozyhome.onlineshop.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService{

    private final DiscountRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final MongoTemplate mongoTemplate;
    
    @Override
    public void createDiscountToProduct(Discount discount, String productSku) {
        Discount savedDiscount = repository.save(discount);
        System.out.println("SKU: " + productSku);
        setDiscountToProduct(productSku, savedDiscount);
    }
    
    private void setDiscountToProduct(String productSku, Discount discount) {
        
        Update update = new Update();
        update.set("discount", discount);

        Query query = Query.query(Criteria.where("subCategories.products.skuCode").is(productSku));
        mongoTemplate.updateFirst(query, update, "category");
        System.out.println("DONE DISCOUNT SETTING TO THE PRODUCT");

//        Aggregation aggregation = newAggregation(match(Criteria.where("subCategories.products.skuCode").is(sku)),
//                unwind("subCategories"), 
//                replaceRoot("subCategories"), 
//                unwind("products"), 
//                match(Criteria.where("products.skuCode").is(sku)),
//                replaceRoot("products"));
        
//        Aggregation aggregation = newAggregation(
//                match(Criteria.where("subCategories.products.skuCode").is(productSku)),
//                unwind("subCategories"),
//                lookup("category", "subCategories.categoryId", "_id", "subCategories.category"),
//                unwind("category"),
//                unwind("subCategories.products"),
//                match(Criteria.where("subCategories.products.skuCode").is(productSku))
//            );
        
//        Aggregation aggregation = newAggregation(
//                match(Criteria.where("subCategories.products.skuCode").is(productSku)),
//                unwind("subCategories"),
//                lookup("category", "_id", "_id", "category"),
//                unwind("category"),
//                unwind("subCategories.products"),
//                match(Criteria.where("subCategories.products.skuCode").is(productSku)),
//                replaceRoot("subCategories.products")
//            );
//        Product product = mongoTemplate
//                .aggregate(aggregation, mongoTemplate.getCollectionName(Category.class), Product.class)
//                .getUniqueMappedResult();
//        System.out.println("PRODUCT BEFORE UPDATE:" + product);
//        product.setDiscount(discount);
//        System.out.println("PRODUCT AFTER UPDATE:" + product);
//        mongoTemplate.save(product, "category");
//        System.out.println("Discount set to product");
    }
   

}
