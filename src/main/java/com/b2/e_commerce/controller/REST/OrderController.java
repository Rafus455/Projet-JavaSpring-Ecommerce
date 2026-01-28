package com.b2.e_commerce.controller.REST;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.b2.e_commerce.dto.OrderRequestDTO;
import com.b2.e_commerce.dto.OrderResponseDTO;
import com.b2.e_commerce.service.OrderService;
import com.b2.e_commerce.service.AuthService;
import com.b2.e_commerce.entity.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @PostMapping
    public OrderResponseDTO create(@Valid @RequestBody OrderRequestDTO dto, Authentication authentication) {
        User user = authService.getAuthenticatedUser(authentication)
                .orElseThrow(() -> new RuntimeException("Utilisateur non authentifié"));
        return orderService.create(dto, user);
    }
}
