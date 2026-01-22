package com.b2.e_commerce;

import com.b2.e_commerce.entity.Category;
import com.b2.e_commerce.entity.Role;
import com.b2.e_commerce.entity.User;
import com.b2.e_commerce.repository.CategoryRepository;
import com.b2.e_commerce.repository.RoleRepository;
import com.b2.e_commerce.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initCategories(CategoryRepository categoryRepository) {
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

    @Bean
    CommandLineRunner initUsers(
            RoleRepository roleRepo,
            UserRepository userRepo,
            PasswordEncoder encoder) {

        return args -> {

            // ROLE
            Role userRole = new Role();
            userRole.setRole("USER");
            roleRepo.save(userRole);

            // USER
            User user = new User();
            user.setMail("test@test.com");
            user.setPassword(encoder.encode("1234"));
            user.setRole(userRole);

            userRepo.save(user);
        };
    }
}
