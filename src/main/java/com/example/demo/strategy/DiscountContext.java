package com.example.demo.strategy;

import org.springframework.stereotype.Component;

/**
 * Strategy Pattern: Context
 *
 * ทำหน้าที่เลือก DiscountStrategy ตามค่า discountType
 * - DIP: พึ่งพา Abstraction (DiscountStrategy) ผ่าน Constructor Injection
 * - OCP: หากต้องการเพิ่มส่วนลดใหม่ สามารถเพิ่ม Strategy ใหม่โดยไม่กระทบโค้ดเดิม
 */
@Component
public class DiscountContext {

    private final DiscountStrategy noDiscountStrategy;
    private final DiscountStrategy memberDiscountStrategy;
    private final DiscountStrategy seasonalSaleStrategy;

    public DiscountContext(NoDiscountStrategy noDiscountStrategy,
                           MemberDiscountStrategy memberDiscountStrategy,
                           SeasonalSaleStrategy seasonalSaleStrategy) {
        this.noDiscountStrategy = noDiscountStrategy;
        this.memberDiscountStrategy = memberDiscountStrategy;
        this.seasonalSaleStrategy = seasonalSaleStrategy;
    }

    public DiscountStrategy resolveStrategy(String discountType) {
        if (discountType == null) {
            return noDiscountStrategy;
        }
        return switch (discountType) {
            case "MEMBER" -> memberDiscountStrategy;
            case "SEASONAL" -> seasonalSaleStrategy;
            default -> noDiscountStrategy;
        };
    }

    public Double calculateFinalPrice(String discountType, Double originalPrice) {
        return resolveStrategy(discountType).calculatePrice(originalPrice);
    }

    public String getDiscountName(String discountType) {
        return resolveStrategy(discountType).getName();
    }
}
