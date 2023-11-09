package com.cozyhome.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cozyhome.onlineshop.model.FavoriteProduct;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Integer>{

}
