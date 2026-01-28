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
import org.springframework.web.bind.annotation.RequestParam;

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
        Category category = categoryRepo.findByNameIgnoreCase(name);
        if (category == null) {
            return "404"; 
        }

        model.addAttribute("category", category);
        List<Product> products = productRepo.findByCategory(category);
        model.addAttribute("products", products);
        model.addAttribute("allCategories", categoryRepo.findAll());

        return "categorie";
    }
    
    @GetMapping("/id/{id}")
    public String voirCategorie(@PathVariable Long id,
                                @RequestParam(required = false) Double min,
                                @RequestParam(required = false) Double max,
                                Model model) {
      
        Category category = categoryRepo.findById(id).orElse(null);
        
        if (category == null) {
            return "redirect:/";
        }
        
        double minPrice = (min != null) ? min : 0;
        double maxPrice = (max != null) ? max : 1000000; 

        List<Product> products = productRepo.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
        
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        
        return "categorie";
    }
}