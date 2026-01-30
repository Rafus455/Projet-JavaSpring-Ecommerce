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
                    return "redirect:/";
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
                                @RequestParam(required = false) Boolean promotion,
                                @RequestParam(required = false) String sort,
                                Model model) {

        Category category = categoryRepo.findById(id).orElse(null);
        if (category == null) return "redirect:/";

        model.addAttribute("allCategories", categoryRepo.findAll());

        double minPrice = (min != null) ? min : 0;
        double maxPrice = (max != null) ? max : 1000000;
        
        List<Product> products = productRepo.findByCategoryAndPriceBetween(category, minPrice, maxPrice);
        
        var stream = products.stream();

        if (Boolean.TRUE.equals(rupture)) {
            stream = stream.filter(p -> p.getStock() == 0);
        }

        if (Boolean.TRUE.equals(nouveau)) {
            stream = stream.filter(Product::isNew);
        }
        
        if (Boolean.TRUE.equals(promotion)) {
            stream = stream.filter(p -> p.getOnSale() > 0);
        }

        if ("asc".equals(sort)) {
            stream = stream.sorted((p1, p2) -> {
                double price1 = (p1.getOnSale() > 0) ? p1.getPrice() * (1 - p1.getOnSale()/100.0) : p1.getPrice();
                double price2 = (p2.getOnSale() > 0) ? p2.getPrice() * (1 - p2.getOnSale()/100.0) : p2.getPrice();
                return Double.compare(price1, price2);
            });
        } else if ("desc".equals(sort)) {
            stream = stream.sorted((p1, p2) -> {
                double price1 = (p1.getOnSale() > 0) ? p1.getPrice() * (1 - p1.getOnSale()/100.0) : p1.getPrice();
                double price2 = (p2.getOnSale() > 0) ? p2.getPrice() * (1 - p2.getOnSale()/100.0) : p2.getPrice();
                return Double.compare(price2, price1);
            });
        }

        products = stream.collect(Collectors.toList());

        model.addAttribute("category", category);
        model.addAttribute("products", products);
        
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);

        return "categorie";
    }
}