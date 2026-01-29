package com.b2.e_commerce.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.b2.e_commerce.entity.Category;
import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.repository.CategoryRepository;
import com.b2.e_commerce.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataMigrationRunner implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataMigrationRunner(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Category> catsToSave = new ArrayList<>();
        for (Category c : categoryRepository.findAll()) {
            if (c.isActive() == null) {
                c.setActive(Boolean.TRUE);
                catsToSave.add(c);
            }
        }
        if (!catsToSave.isEmpty()) {
            categoryRepository.saveAll(catsToSave);
            System.out.println("DataMigrationRunner: activated " + catsToSave.size() + " categories (was NULL)");
        }

        List<Product> prodsToSave = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (p.isActive() == null) {
                p.setActive(Boolean.TRUE);
                prodsToSave.add(p);
            }
        }
        if (!prodsToSave.isEmpty()) {
            productRepository.saveAll(prodsToSave);
            System.out.println("DataMigrationRunner: activated " + prodsToSave.size() + " products (was NULL)");
        }
    }
}
