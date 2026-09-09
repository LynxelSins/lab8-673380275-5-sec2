package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * Entity: Review
 *
 * เก็บบันทึกรีวิวสินค้า (Part B - ความสัมพันธ์ 1:N)
 * ออกแบบตามหลัก Open/Closed Principle (OCP) ทำให้สามารถเพิ่มรีวิวได้ไม่จำกัด
 * โดยไม่ต้องแก้ไข schema ของตาราง Product
 * ฝั่ง Many (Review) เป็นผู้ถือครอง Foreign Key (product_id) ผ่าน @ManyToOne
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reviewer;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 1000)
    private String comment;

    @Column(name = "review_date", nullable = false)
    private LocalDate reviewDate;

    // FK อยู่ที่ฝั่ง Many เสมอ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public Review() {
        this.reviewDate = LocalDate.now();
    }

    public Review(String reviewer, Integer rating, String comment, LocalDate reviewDate, Product product) {
        this.reviewer = reviewer;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate != null ? reviewDate : LocalDate.now();
        this.product = product;
    }

    // ─── Getters & Setters ───

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
