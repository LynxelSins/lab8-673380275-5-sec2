package com.example.demo.strategy;

/**
 * Strategy Pattern: Interface กลางสำหรับกลยุทธ์การคำนวณส่วนลดราคาสินค้า
 *
 * - ISP (Interface Segregation Principle): มีเฉพาะ method ที่จำเป็นเท่านั้น
 * - LSP (Liskov Substitution Principle): ทุกคลาสที่ implement interface นี้
 *   สามารถใช้แทนกันได้อย่างสมบูรณ์ผ่าน DiscountContext
 */
public interface DiscountStrategy {

    /**
     * คำนวณราคาสุทธิหลังหักส่วนลด
     *
     * @param originalPrice ราคาปกติของสินค้า
     * @return ราคาสุทธิหลังหักส่วนลด
     */
    Double calculatePrice(Double originalPrice);

    /**
     * ชื่อของกลยุทธ์ส่วนลด สำหรับแสดงผลใน UI
     *
     * @return ชื่อส่วนลด เช่น "ราคาปกติ", "ส่วนลดสมาชิก 10%"
     */
    String getName();
}
