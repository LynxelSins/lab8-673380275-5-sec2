package com.example.demo.strategy;

import org.springframework.stereotype.Component;

/**
 * Concrete Strategy: ราคาปกติ ไม่มีส่วนลด (0%)
 */
@Component
public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public Double calculatePrice(Double originalPrice) {
        return originalPrice != null ? originalPrice : 0.0;
    }

    @Override
    public String getName() {
        return "ราคาปกติ (0%)";
    }
}
