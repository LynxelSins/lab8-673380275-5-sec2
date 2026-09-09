package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository: ProductRepository
 *
 * ทำหน้าที่เข้าถึงฐานข้อมูลสำหรับ Product Entity
 * - ISP (Interface Segregation Principle): แยก Repository สำหรับแต่ละ Entity
 * - DIP (Dependency Inversion Principle): Service พึ่งพา Repository Interface นี้
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
