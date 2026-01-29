package com.b2.e_commerce.service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.b2.e_commerce.dto.ProductRequestDTO;
import com.b2.e_commerce.dto.ProductResponseDTO;
import com.b2.e_commerce.entity.Category;
import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.exception.ResourceNotFoundException;
import com.b2.e_commerce.repository.CategoryRepository;
import com.b2.e_commerce.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final com.b2.e_commerce.repository.OrderItemRepository orderItemRepository;

    private static final String UPLOAD_DIR = "uploads/products/";

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            com.b2.e_commerce.repository.OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // READ
    public List<ProductResponseDTO> findAll() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Produit avec id " + id + " introuvable"));

        return mapToResponse(product);
    }

    // CREATE
    public ProductResponseDTO create(ProductRequestDTO dto, MultipartFile image) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category introuvable"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setOnSale(dto.getOnSale());
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        if (image != null && !image.isEmpty()) {
            product.setPathImage(saveImage(image));
        }

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    // UPDATE
    public ProductResponseDTO update(Long id, ProductRequestDTO dto, MultipartFile image) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Produit avec id " + id + " introuvable"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category introuvable"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setOnSale(dto.getOnSale());
        product.setCategory(category);
        product.setUpdatedAt(LocalDateTime.now());

        if (image != null && !image.isEmpty()) {
            deleteImageIfExists(product.getPathImage());
            product.setPathImage(saveImage(image));
        }

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    // DELETE
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Produit avec id " + id + " introuvable"));

        if (orderItemRepository.existsByProduct(product)) {
            product.setActive(false);
            productRepository.save(product);
            return;
        }

        deleteImageIfExists(product.getPathImage());
        productRepository.delete(product);
    }

    // IMAGE MANAGEMENT
    private String saveImage(MultipartFile image) {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String extension = Objects.requireNonNull(image.getOriginalFilename())
                    .substring(image.getOriginalFilename().lastIndexOf("."));

            String filename = UUID.randomUUID() + extension;
            Path path = Paths.get(UPLOAD_DIR + filename);

            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/products/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de l'image", e);
        }
    }

    private void deleteImageIfExists(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) return;

        try {
            Path path = Paths.get(imagePath.replace("/uploads/", "uploads/"));
            Files.deleteIfExists(path);
        } catch (IOException e) {
        }
    }

    // DTO MAPPING
    private ProductResponseDTO mapToResponse(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setOnSale(product.getOnSale());
        dto.setPathImage(product.getPathImage());
        dto.setActive(Boolean.TRUE.equals(product.isActive()));
        dto.setCategoryName(product.getCategory().getName());
        dto.setCategoryId(product.getCategory().getId());
        return dto;
    }
}
