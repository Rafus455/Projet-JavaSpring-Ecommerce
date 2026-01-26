package com.b2.e_commerce.controller.REST;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.b2.e_commerce.dto.ProductRequestDTO;
import com.b2.e_commerce.dto.ProductResponseDTO;
import com.b2.e_commerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/product")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    // CREATE WITH IMAGE
    @PostMapping(consumes = "multipart/form-data")
    public ProductResponseDTO create(
            @Valid @ModelAttribute ProductRequestDTO dto,
            @RequestParam("image") MultipartFile image
    ) {
        return productService.create(dto, image);
    }

    // UPDATE WITH IMAGE
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ProductResponseDTO update(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductRequestDTO dto,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        return productService.update(id, dto, image);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}

