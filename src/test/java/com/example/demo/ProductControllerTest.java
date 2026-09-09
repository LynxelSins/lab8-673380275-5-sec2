package com.example.demo;

import com.example.demo.controller.ProductController;
import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ProductControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void testWebFlow() throws Exception {
        // 1. Check GET /products
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"));

        // 2. Check GET /products/add
        mockMvc.perform(get("/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/add"))
                .andExpect(model().attributeExists("product"));

        // 3. Check POST /products/save
        mockMvc.perform(post("/products/save")
                .param("name", "Test Item")
                .param("category", "Electronics")
                .param("brand", "TestBrand")
                .param("price", "199.99")
                .param("stock", "5")
                .param("discountType", "NONE")
                .param("detail.warranty", "2 Years")
                .param("detail.description", "Good item"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        Product created = productService.getAllProducts().stream()
                .filter(p -> "Test Item".equals(p.getName()))
                .findFirst().orElseThrow();

        // 4. Check GET /products/edit/{id}
        mockMvc.perform(get("/products/edit/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("products/edit"))
                .andExpect(model().attributeExists("product"));

        // 5. Check GET /products/delete/{id}
        mockMvc.perform(get("/products/delete/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("products/delete"))
                .andExpect(model().attributeExists("product"));

        // 6. Check POST /products/{id}/reviews
        mockMvc.perform(post("/products/" + created.getId() + "/reviews")
                .param("reviewer", "Tester")
                .param("rating", "5")
                .param("comment", "Great product!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        // 7. Check GET /products rendering with item and review
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"));

        // 8. Check POST /products/delete/{id}
        mockMvc.perform(post("/products/delete/" + created.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }
}
