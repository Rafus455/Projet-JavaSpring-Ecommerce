package com.b2.e_commerce.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.exception.ResourceNotFoundException;
import com.b2.e_commerce.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    public Product findById(Long id) {
    	return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit avec id " + id + " introuvable"));
    }

}

