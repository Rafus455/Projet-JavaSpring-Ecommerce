package com.b2.e_commerce.repository;

import com.b2.e_commerce.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
