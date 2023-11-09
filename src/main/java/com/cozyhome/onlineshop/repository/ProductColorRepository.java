package com.cozyhome.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.model.ProductColor;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor, Integer>{

}
