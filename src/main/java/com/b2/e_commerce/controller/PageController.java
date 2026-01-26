package com.b2.e_commerce.controller;

import com.b2.e_commerce.entity.User;
import com.b2.e_commerce.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final UserRepository userRepository;

    public PageController(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @GetMapping("/ordinateur")
    public String ordinateur() {
        return "Ordinateur";
    }

    @GetMapping("/account")
    public String account(Authentication authentication, Model model) {

    	if (authentication != null && authentication.isAuthenticated()) {
    		String mail = authentication.getName();
            User user = userRepository.findByMail(mail).orElseThrow();

            model.addAttribute("user", user);
            return "account";
        }
    	
    	return "redirect:/login";
    }
    
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
}
