package com.cozyhome.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozyhome.onlineshop.model.BasketItem;

public interface BasketRepository extends JpaRepository<BasketItem, Integer>{

}
