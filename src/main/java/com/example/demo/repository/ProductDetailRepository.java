package com.example.demo.repository;

import com.example.demo.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository: ProductDetailRepository
 *
 * ทำหน้าที่เข้าถึงฐานข้อมูลสำหรับ ProductDetail Entity
 */
@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
