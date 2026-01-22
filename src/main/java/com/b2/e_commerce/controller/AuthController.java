package com.b2.e_commerce.controller;

import com.b2.e_commerce.entity.Role;
import com.b2.e_commerce.entity.User;
import com.b2.e_commerce.repository.RoleRepository;
import com.b2.e_commerce.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // LOGIN 
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // REGISTER 
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
    	
    	if (userRepository.findByMail(user.getMail()).isPresent()) {
            return "redirect:/register?error";
        }

        Role role = roleRepository.findByRole("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return "redirect:/login";
    }
}
