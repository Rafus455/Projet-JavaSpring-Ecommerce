package com.b2.e_commerce.controller.REST;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.b2.e_commerce.dto.ProductRequestDTO;
import com.b2.e_commerce.dto.ProductResponseDTO;
import com.b2.e_commerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET ALL
    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return productService.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ProductResponseDTO getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    // CREATE
    @PostMapping
    public ProductResponseDTO create(@Valid @RequestBody ProductRequestDTO dto) {
        return productService.create(dto);
    }

    // MODIFY
    @PutMapping("/{id}")
    public ProductResponseDTO modify(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        return productService.update(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
       productService.delete(id);
    }
}
