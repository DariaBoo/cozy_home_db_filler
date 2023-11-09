package com.cozyhome.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{

}
