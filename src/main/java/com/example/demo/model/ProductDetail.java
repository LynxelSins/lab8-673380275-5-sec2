package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entity: ProductDetail
 *
 * เก็บข้อมูลเสริมของสินค้า (Part A - ความสัมพันธ์ 1:1)
 * ออกแบบตามหลัก Single Responsibility Principle (SRP) เพื่อแยกข้อมูลเฉพาะทาง
 * เช่น ขนาด, น้ำหนัก, การรับประกัน ไม่ให้กระจุกตัวใน Product
 * เป็นฝ่าย inverse ของความสัมพันธ์ 1:1 (mappedBy="detail")
 */
@Entity
@Table(name = "product_details")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String description;

    @Column(length = 100)
    private String warranty;

    private Double weight;

    @Column(length = 100)
    private String dimensions;

    @Column(name = "manufactured_country", length = 100)
    private String manufacturedCountry;

    @OneToOne(mappedBy = "detail")
    private Product product;

    public ProductDetail() {
    }

    public ProductDetail(String description, String warranty, Double weight, String dimensions, String manufacturedCountry) {
        this.description = description;
        this.warranty = warranty;
        this.weight = weight;
        this.dimensions = dimensions;
        this.manufacturedCountry = manufacturedCountry;
    }

    // ─── Getters & Setters ───

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getManufacturedCountry() {
        return manufacturedCountry;
    }

    public void setManufacturedCountry(String manufacturedCountry) {
        this.manufacturedCountry = manufacturedCountry;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
