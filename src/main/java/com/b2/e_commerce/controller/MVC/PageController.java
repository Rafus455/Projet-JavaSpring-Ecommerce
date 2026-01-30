package com.b2.e_commerce.controller.MVC;

import com.b2.e_commerce.dto.ProductResponseDTO;
import com.b2.e_commerce.service.AuthService;
import com.b2.e_commerce.service.ProductService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    private final AuthService authService;
    private final ProductService productService;

    public PageController(AuthService authService, ProductService productService) {
        this.authService = authService;
        this.productService = productService;
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
    	if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/account";
        }

        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/account")
    public String account(Authentication authentication, Model model) {

    	return authService.getAuthenticatedUser(authentication)
    	        .map(user -> {
    	            model.addAttribute("user", user);
    	            return "account";
    	        })
    	        .orElse("redirect:/logout");
    }

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/panier")
    public String pannier(Authentication authentication, Model model) {

    	return authService.getAuthenticatedUser(authentication)
    	        .map(user -> {
    	            model.addAttribute("user", user);
    	            return "panier";
    	        })
    	        .orElse("redirect:/logout");
    }
    
    @GetMapping("/produit/{id}")
    public String produit(Authentication authentication, @PathVariable Long id, Model model) {
    	return authService.getAuthenticatedUser(authentication)
    	        .map(user -> {

    	        	ProductResponseDTO product = productService.findById(id);

    	            model.addAttribute("product", product);
    	            model.addAttribute("category", product.getCategoryName());
    	            return "produit";
    	        })
    	        .orElse("redirect:/logout");
    }
}
