package com.b2.e_commerce.controller.REST;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.b2.e_commerce.dto.DashboardStatsDTO;
import com.b2.e_commerce.repository.UserRepository;
import com.b2.e_commerce.repository.ProductRepository;
import com.b2.e_commerce.repository.OrderRepository;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    public AdminDashboardController(
            UserRepository userRepo,
            ProductRepository productRepo,
            OrderRepository orderRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    @GetMapping
    public DashboardStatsDTO stats() {
        return new DashboardStatsDTO(
            userRepo.count(),
            productRepo.count(),
            orderRepo.count()
        );
    }
}
