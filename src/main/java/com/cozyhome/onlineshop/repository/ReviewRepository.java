package com.cozyhome.onlineshop.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cozyhome.onlineshop.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
	
    List<Review> findReviewsByProductSkuCode(String productSkuCode);
}
