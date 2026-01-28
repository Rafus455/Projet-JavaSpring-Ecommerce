package com.b2.e_commerce.controller.REST;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.b2.e_commerce.dto.OrderRequestDTO;
import com.b2.e_commerce.dto.OrderResponseDTO;
import com.b2.e_commerce.dto.OrderStatusUpdateDTO;
import com.b2.e_commerce.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // GET ALL
    @GetMapping
    public List<OrderResponseDTO> getAll() {
        return orderService.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public OrderResponseDTO getById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    // MODIFY
    @PutMapping("/{id}/status")
    public OrderResponseDTO updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateDTO dto) {
        return orderService.updateStatus(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
    	orderService.delete(id);
    }
}
