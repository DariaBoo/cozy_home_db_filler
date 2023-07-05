package com.cozyhome.onlineshop.productservice.service;

import com.cozyhome.onlineshop.productservice.model.Discount;

public interface DiscountService {

    void createDiscountToProduct(Discount discount, String productSku);
}
    
