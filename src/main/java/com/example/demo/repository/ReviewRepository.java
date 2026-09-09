package com.example.demo.repository;

import com.example.demo.model.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository: ReviewRepository
 *
 * ทำหน้าที่เข้าถึงฐานข้อมูลสำหรับ Review Entity
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
}
