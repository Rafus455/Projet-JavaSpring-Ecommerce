package com.b2.e_commerce.controller.REST;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.service.ProductService;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET ALL
    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    // CREATE
    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }
}
