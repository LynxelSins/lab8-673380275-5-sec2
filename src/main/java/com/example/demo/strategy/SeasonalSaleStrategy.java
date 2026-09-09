package com.example.demo.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

/**
 * Concrete Strategy: ส่วนลดเทศกาล 20%
 */
@Component
public class SeasonalSaleStrategy implements DiscountStrategy {

    @Override
    public Double calculatePrice(Double originalPrice) {
        if (originalPrice == null) {
            return 0.0;
        }
        BigDecimal price = BigDecimal.valueOf(originalPrice);
        BigDecimal discount = price.multiply(BigDecimal.valueOf(0.20));
        BigDecimal finalPrice = price.subtract(discount).setScale(2, RoundingMode.HALF_UP);
        return finalPrice.doubleValue();
    }

    @Override
    public String getName() {
        return "ส่วนลดเทศกาล (20%)";
    }
}
