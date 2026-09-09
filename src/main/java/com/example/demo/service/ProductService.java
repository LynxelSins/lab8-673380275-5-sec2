package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductDetailRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.strategy.DiscountContext;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service (Business Logic Layer)
 *
 * - SRP: รับผิดชอบ business logic ของ Product, ProductDetail, Review
 *   และเชื่อมต่อกับ DiscountContext สำหรับคำนวณราคาส่วนลด
 * - DIP: พึ่งพา Abstractions (Repositories & DiscountContext) ผ่าน Constructor Injection
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ReviewRepository reviewRepository;
    private final DiscountContext discountContext;

    public ProductService(ProductRepository productRepository,
                          ProductDetailRepository productDetailRepository,
                          ReviewRepository reviewRepository,
                          DiscountContext discountContext) {
        this.productRepository = productRepository;
        this.productDetailRepository = productDetailRepository;
        this.reviewRepository = reviewRepository;
        this.discountContext = discountContext;
    }

    /**
     * ดึงรายการสินค้าทั้งหมด พร้อมคำนวณราคาสุทธิและชื่อส่วนลด
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        products.forEach(this::applyDiscount);
        return products;
    }

    /**
     * ดึงข้อมูลสินค้าตาม ID พร้อมคำนวณราคาสุทธิ
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบสินค้า ID: " + id));
        applyDiscount(product);
        return product;
    }

    /**
     * บันทึกสินค้าใหม่ หรืออัปเดตสินค้าเดิม
     * จัดการความสัมพันธ์ 1:1 กับ ProductDetail (CascadeType.ALL)
     */
    public Product saveProduct(Product product) {
        if (product.getDetail() != null) {
            product.getDetail().setProduct(product);
        }
        return productRepository.save(product);
    }

    /**
     * อัปเดตสินค้าจากฟอร์ม Edit
     */
    public Product updateProduct(Long id, Product formProduct) {
        Product existing = getProductById(id);

        existing.setName(formProduct.getName());
        existing.setCategory(formProduct.getCategory());
        existing.setBrand(formProduct.getBrand());
        existing.setStock(formProduct.getStock());
        existing.setPrice(formProduct.getPrice());
        existing.setDiscountType(formProduct.getDiscountType());

        // อัปเดตข้อมูล 1:1 ProductDetail
        if (formProduct.getDetail() != null) {
            ProductDetail detail = existing.getDetail();
            if (detail == null) {
                detail = new ProductDetail();
                detail.setProduct(existing);
                existing.setDetail(detail);
            }
            detail.setDescription(formProduct.getDetail().getDescription());
            detail.setWarranty(formProduct.getDetail().getWarranty());
            detail.setWeight(formProduct.getDetail().getWeight());
            detail.setDimensions(formProduct.getDetail().getDimensions());
            detail.setManufacturedCountry(formProduct.getDetail().getManufacturedCountry());
        }

        return productRepository.save(existing);
    }

    /**
     * ลบสินค้าตาม ID (Cascades delete detail and reviews)
     */
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * เพิ่ม Review ให้กับสินค้า (1:N)
     */
    public Review addReview(Long productId, Review review) {
        Product product = getProductById(productId);
        review.setId(null);
        product.addReview(review);
        return reviewRepository.save(review);
    }

    /**
     * ดึงรายการรีวิวตาม Product ID
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    /**
     * Helper คำนวณราคาและชื่อส่วนลด
     */
    private void applyDiscount(Product product) {
        product.setFinalPrice(discountContext.calculateFinalPrice(product.getDiscountType(), product.getPrice()));
        product.setDiscountName(discountContext.getDiscountName(product.getDiscountType()));
    }
}
