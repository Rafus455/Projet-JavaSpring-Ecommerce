package com.b2.e_commerce.repository;

import com.b2.e_commerce.entity.OrderItem;
import com.b2.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);
    boolean existsByProduct(Product product);

    List<OrderItem> findAllByOrderId(Long orderId);
}
