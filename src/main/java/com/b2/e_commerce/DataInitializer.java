package com.b2.e_commerce;

//import com.b2.e_commerce.entity.Category;
import com.b2.e_commerce.entity.Role;
import com.b2.e_commerce.entity.User;
//import com.b2.e_commerce.repository.CategoryRepository;
import com.b2.e_commerce.repository.RoleRepository;
import com.b2.e_commerce.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
//
//    @Bean
//    CommandLineRunner init(CategoryRepository categoryRepository) {
//        return args -> {
//            if (categoryRepository.count() == 0) {
//                Category c = new Category();
//                c.setName("Informatique");
//                c.setDescription("Produits informatiques");
//                c.setType("TECH");
//                categoryRepository.save(c);
//            }
//        };
//    }
//    
    @Bean
    CommandLineRunner initUsers(
            RoleRepository roleRepo,
            UserRepository userRepo,
            PasswordEncoder encoder) {

        return args -> {
        	// ROLE USER
            Role userRole = roleRepo.findByRole("USER")
            .orElseGet(() -> {
                Role r = new Role();
                r.setRole("USER");
                return roleRepo.save(r);
            });

            // USER DE TEST
            if (userRepo.findByMail("test@test.com").isEmpty()) {
                User user = new User();
                user.setName("Test");
                user.setFirstname("Test");
                user.setMail("test@test.com");
                user.setPassword(encoder.encode("1234"));
                user.setRole(userRole);
                userRepo.save(user);
            }
            

            // ROLE Admin
            Role adminRole = roleRepo.findByRole("ADMIN")
            .orElseGet(() -> {
                Role r = new Role();
                r.setRole("ADMIN");
                return roleRepo.save(r);
            });
            
            // USER DE TEST
            if (userRepo.findByMail("a@gmail.com").isEmpty()) {
                User user = new User();
                user.setName("Admin");
                user.setFirstname("Admin");
                user.setMail("a@gmail.com");
                user.setPassword(encoder.encode("a@gmail.com"));
                user.setRole(adminRole);
                userRepo.save(user);
            }
        };
    }

}
