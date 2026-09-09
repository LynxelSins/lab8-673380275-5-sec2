package com.example.demo;

import com.example.demo.strategy.DiscountContext;
import com.example.demo.strategy.MemberDiscountStrategy;
import com.example.demo.strategy.NoDiscountStrategy;
import com.example.demo.strategy.SeasonalSaleStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductStrategyTest {

    @Test
    void testDiscounts() {
        NoDiscountStrategy noDiscount = new NoDiscountStrategy();
        MemberDiscountStrategy memberDiscount = new MemberDiscountStrategy();
        SeasonalSaleStrategy seasonalSale = new SeasonalSaleStrategy();
        DiscountContext context = new DiscountContext(noDiscount, memberDiscount, seasonalSale);

        Double originalPrice = 1000.0;

        assertEquals(1000.0, context.calculateFinalPrice("NONE", originalPrice));
        assertEquals(900.0, context.calculateFinalPrice("MEMBER", originalPrice));
        assertEquals(800.0, context.calculateFinalPrice("SEASONAL", originalPrice));

        assertEquals("ราคาปกติ (0%)", context.getDiscountName("NONE"));
        assertEquals("ส่วนลดสมาชิก (10%)", context.getDiscountName("MEMBER"));
        assertEquals("ส่วนลดเทศกาล (20%)", context.getDiscountName("SEASONAL"));
    }
}
