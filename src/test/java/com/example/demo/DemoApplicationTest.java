package com.example.demo;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        productRepository.deleteAll();
        productDetailRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertNotNull(productService);
    }

    @Test
    void testCreateReadUpdateDeleteWithRelationships() {
        // 1. Create Product with 1:1 ProductDetail
        Product product = new Product("iPhone 15 Pro (673380275-5 SEC 2)", "Electronics", "Apple", 10, 40000.0, "MEMBER");
        ProductDetail detail = new ProductDetail("Titanium frame, A17 Pro", "1 Year", 0.187, "14.6 x 7.0 x 0.8 cm", "USA");
        product.setDetail(detail);

        Product savedProduct = productService.saveProduct(product);
        assertNotNull(savedProduct.getId());
        assertNotNull(savedProduct.getDetail().getId());

        // 2. Read and verify Strategy calculation (MEMBER: 10% discount)
        Product fetched = productService.getProductById(savedProduct.getId());
        assertEquals(36000.0, fetched.getFinalPrice());
        assertEquals("ส่วนลดสมาชิก (10%)", fetched.getDiscountName());
        assertEquals("Titanium frame, A17 Pro", fetched.getDetail().getDescription());

        // 3. Add Reviews (1:N)
        Review review1 = new Review("Alice", 5, "Amazing phone!", LocalDate.now(), null);
        Review review2 = new Review("Bob", 4, "Good battery life", LocalDate.now(), null);
        productService.addReview(savedProduct.getId(), review1);
        productService.addReview(savedProduct.getId(), review2);

        List<Review> reviews = productService.getReviewsByProductId(savedProduct.getId());
        assertEquals(2, reviews.size());

        // 4. Update Product
        fetched.setName("iPhone 15 Pro Max");
        fetched.setPrice(45000.0);
        fetched.setDiscountType("SEASONAL");
        fetched.getDetail().setDescription("Updated description");
        productService.updateProduct(savedProduct.getId(), fetched);

        Product updated = productService.getProductById(savedProduct.getId());
        assertEquals("iPhone 15 Pro Max", updated.getName());
        assertEquals(36000.0, updated.getFinalPrice()); // 45000 * 0.8 = 36000
        assertEquals("ส่วนลดเทศกาล (20%)", updated.getDiscountName());
        assertEquals("Updated description", updated.getDetail().getDescription());

        // 5. Delete Product and verify Cascade Delete
        productService.deleteProduct(savedProduct.getId());
        assertEquals(0, productRepository.count());
        assertEquals(0, productDetailRepository.count());
        assertEquals(0, reviewRepository.count());
    }
}
