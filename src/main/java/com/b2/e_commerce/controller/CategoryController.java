package com.b2.e_commerce.controller;

import com.b2.e_commerce.entity.Category;
import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.repository.CategoryRepository;
import com.b2.e_commerce.repository.ProductRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categorie")
public class CategoryController {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    public CategoryController(CategoryRepository categoryRepo, ProductRepository productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    @GetMapping("/{name}")
    public String category(@PathVariable String name, Model model) {

        // Récupération de la catégorie
        Category category = categoryRepo.findByNameIgnoreCase(name);
        if (category == null) {
            return "404"; // Page d'erreur si catégorie inexistante
        }

        model.addAttribute("category", category);

        // Récupération des produits de cette catégorie
        List<Product> products = productRepo.findByCategory(category);
        model.addAttribute("products", products);

        return "categorie"; // fichier Thymeleaf
    }
}
