package com.b2.e_commerce.controller;

import com.b2.e_commerce.entity.Category;
import com.b2.e_commerce.entity.Product;
import com.b2.e_commerce.repository.CategoryRepository;
import com.b2.e_commerce.repository.ProductRepository;
import com.b2.e_commerce.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categorie")
public class CategoryController {

    private final AuthService authService;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    public CategoryController(CategoryRepository categoryRepo, ProductRepository productRepo, AuthService authService) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.authService = authService;
    }

    @GetMapping("/{name}")
    public String category(@PathVariable String name, Model model, Authentication authentication) {
    	return authService.getAuthenticatedUser(authentication)
	        .map(user -> {
	        	Category category = categoryRepo.findByNameIgnoreCase(name);
	            if (category == null) {
	                return "404"; 
	            }
	
	            model.addAttribute("category", category);
	            List<Product> products = productRepo.findByCategory(category);
	            model.addAttribute("products", products);
	            model.addAttribute("allCategories", categoryRepo.findAll());
	
	            return "categorie";
	        })
	        .orElse("redirect:/logout");
    }

    @GetMapping("/id/{id}")
    public String voirCategorie(@PathVariable Long id,
                                @RequestParam(required = false) Double min,
                                @RequestParam(required = false) Double max,
                                @RequestParam(required = false) Boolean nouveau,
                                @RequestParam(required = false) Boolean rupture,
                                Model model) {

        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) return "redirect:/";

        model.addAttribute("allCategories", categoryRepo.findAll());

        double minPrice = (min != null) ? min : 0;
        double maxPrice = (max != null) ? max : 1000000;
        List<Product> products = productRepo.findByCategoryAndPriceBetween(category, minPrice, maxPrice);

        if (Boolean.TRUE.equals(rupture)) {
            products = products.stream()
                    .filter(p -> p.getStock() == 0)
                    .collect(Collectors.toList());
        }

        if (Boolean.TRUE.equals(nouveau)) {
            products = products.stream()
                    .filter(Product::isNew)
                    .collect(Collectors.toList());
        }

        model.addAttribute("category", category);
        model.addAttribute("products", products);

        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "categorie";
    }
}