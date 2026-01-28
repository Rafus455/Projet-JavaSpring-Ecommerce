package com.b2.e_commerce.controller.REST;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.b2.e_commerce.dto.ProductResponseDTO;
import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.repository.ProductRepository;
import com.b2.e_commerce.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class PublicProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public PublicProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping("/new")
    public List<ProductResponseDTO> getNewest(@RequestParam(name = "limit", defaultValue = "3") int limit) {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return products.stream()
                .limit(Math.max(0, limit))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/best")
    public List<ProductResponseDTO> getBests() {
        return productService.findAll();
    }

    private ProductResponseDTO mapToDto(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setOnSale(product.getOnSale());
        dto.setPathImage(product.getPathImage());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        return dto;
    }
}
