package com.b2.e_commerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.b2.e_commerce.dto.ProductRequestDTO;
import com.b2.e_commerce.dto.ProductResponseDTO;
import com.b2.e_commerce.entity.Category;
import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.exception.ResourceNotFoundException;
import com.b2.e_commerce.repository.ProductRepository;
import com.b2.e_commerce.repository.CategoryRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
            CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit avec id " + id + " introuvable"));

        return mapToResponse(product);
    }

    public ProductResponseDTO create(ProductRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setOnSale(dto.getOnSale());
        product.setPathImage(dto.getPathImage());
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
        	.orElseThrow(() -> new ResourceNotFoundException("Produit avec id " + id + " introuvable")  );

        Category category = categoryRepository.findById(dto.getCategoryId())
            	.orElseThrow(() -> new ResourceNotFoundException("Category not found")  );

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setOnSale(dto.getOnSale());
        product.setPathImage(dto.getPathImage());
        product.setCategory(category);

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produit avec id " + id + " introuvable") );

        productRepository.delete(product);
    }

    private ProductResponseDTO mapToResponse(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setOnSale(product.isOnSale());
        dto.setPathImage(product.getPathImage());
        dto.setCategoryName(product.getCategory().getName());
        return dto;
    }
}