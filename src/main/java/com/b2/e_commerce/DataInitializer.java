package com.b2.e_commerce;

import com.b2.e_commerce.entity.Category;
import com.b2.e_commerce.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                Category c = new Category();
                c.setName("Informatique");
                c.setDescription("Produits informatiques");
                c.setType("TECH");
                categoryRepository.save(c);
            }
        };
    }
}
