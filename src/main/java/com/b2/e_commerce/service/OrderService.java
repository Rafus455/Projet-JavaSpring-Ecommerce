package com.b2.e_commerce.service;

import com.b2.e_commerce.dto.OrderRequestDTO;
import com.b2.e_commerce.dto.OrderResponseDTO;
import com.b2.e_commerce.dto.OrderStatusUpdateDTO;
import com.b2.e_commerce.entity.CustomerOrder;
import com.b2.e_commerce.entity.User;
import com.b2.e_commerce.exception.InvalidOrderStatusTransitionException;
import com.b2.e_commerce.exception.ResourceNotFoundException;
import com.b2.e_commerce.repository.OrderRepository;
import com.b2.e_commerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO findById(Long id) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande avec id " + id + " introuvable"));

        return mapToResponse(order);
    }

    public OrderResponseDTO create(OrderRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Commande avec id " + dto.getUserId() + " introuvable"));

        CustomerOrder order = new CustomerOrder();
        order.setUser(user);
        order.setPriceTotal(dto.getPriceTotal());
        order.setStatus(dto.getStatus());
        order.setOrderDate(LocalDateTime.now());

        CustomerOrder saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    public OrderResponseDTO updateStatus(Long id, OrderStatusUpdateDTO dto) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande avec id " + id + " introuvable"));

        if (!order.getStatus().canTransitionTo(dto.getStatus())) {
        	throw new InvalidOrderStatusTransitionException(
        	        "Transition de statut interdite de " + order.getStatus() + " vers " + dto.getStatus()
        	    );
        }

        order.setStatus(dto.getStatus());
        CustomerOrder saved = orderRepository.save(order);

        return mapToResponse(saved);
    }

    public void delete(Long id) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order avec id " + id + " introuvable"));

        orderRepository.delete(order);
    }

    private OrderResponseDTO mapToResponse(CustomerOrder order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setPriceTotal(order.getPriceTotal());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }
}
