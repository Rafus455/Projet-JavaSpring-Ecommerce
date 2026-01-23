package com.b2.e_commerce.controller;

import com.b2.e_commerce.entity.User;
import com.b2.e_commerce.repository.UserRepository;
import com.b2.e_commerce.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserRepository userRepository;
    private final UserService userService;

    public AccountController(UserRepository userRepository,
                             UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping
    public String updateAccount(Authentication authentication,
                                @RequestParam String name,
                                @RequestParam String firstname,
                                @RequestParam String mail,
                                @RequestParam(required = false) String password) {

        User user = userRepository.findByMail(authentication.getName())
                .orElseThrow();

        userService.updateAccount(user, name, firstname, mail, password);

        return "redirect:/account?success";
    }
}
