package com.b2.e_commerce.repository;

import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCategory(Category category);
    
    List<Product> findByCategoryAndPriceBetween(Category category, double min, double max);
    
    List<Product> findByActiveTrue();

    List<Product> findByCategoryAndActiveTrue(Category category);
}