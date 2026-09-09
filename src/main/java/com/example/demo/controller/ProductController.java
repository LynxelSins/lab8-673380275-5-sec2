package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.model.Review;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller (Presentation Layer)
 *
 * - SRP: รับผิดชอบการจัดการ HTTP Request และ View Routing
 * - DIP: พึ่งพา ProductService ผ่าน Constructor Injection
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * READ: แสดงรายการสินค้าทั้งหมด
     */
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }

    /**
     * CREATE: แสดงฟอร์มเพิ่มสินค้าใหม่
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        Product product = new Product();
        product.setDetail(new ProductDetail());
        model.addAttribute("product", product);
        return "products/add";
    }

    /**
     * CREATE: บันทึกสินค้าใหม่พร้อม ProductDetail ลงฐานข้อมูล
     */
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              RedirectAttributes redirectAttributes) {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "เพิ่มสินค้า \"" + product.getName() + "\" สำเร็จ");
        return "redirect:/products";
    }

    /**
     * UPDATE: แสดงฟอร์มแก้ไขสินค้า
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product.getDetail() == null) {
            product.setDetail(new ProductDetail());
        }
        model.addAttribute("product", product);
        return "products/edit";
    }

    /**
     * UPDATE: บันทึกการแก้ไขสินค้า
     */
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute("product") Product product,
                                RedirectAttributes redirectAttributes) {
        productService.updateProduct(id, product);
        redirectAttributes.addFlashAttribute("message", "อัปเดตข้อมูลสินค้า \"" + product.getName() + "\" สำเร็จ");
        return "redirect:/products";
    }

    /**
     * DELETE: แสดงหน้ายืนยันการลบสินค้า
     */
    @GetMapping("/delete/{id}")
    public String showDeleteConfirm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "products/delete";
    }

    /**
     * DELETE: ดำเนินการลบสินค้าออกจากฐานข้อมูล
     */
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "ลบสินค้าสำเร็จ");
        return "redirect:/products";
    }

    /**
     * 1:N REVIEW: เพิ่มรีวิวให้สินค้า (โบนัสฟังก์ชัน)
     */
    @PostMapping("/{id}/reviews")
    public String addReview(@PathVariable("id") Long id,
                            @ModelAttribute("review") Review review,
                            RedirectAttributes redirectAttributes) {
        review.setId(null);
        productService.addReview(id, review);
        redirectAttributes.addFlashAttribute("message", "เพิ่มรีวิวสำหรับสินค้าเรียบร้อยแล้ว");
        return "redirect:/products";
    }
}
