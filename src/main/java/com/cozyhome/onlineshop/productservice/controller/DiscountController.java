package com.cozyhome.onlineshop.productservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cozyhome.onlineshop.productservice.model.Discount;
import com.cozyhome.onlineshop.productservice.service.DiscountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class DiscountController {

    private final DiscountService service;
    
    @PostMapping("/discount")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDiscount(@RequestParam String productSku, @RequestBody Discount discount) {
        service.createDiscountToProduct(discount, productSku);
    }
}
