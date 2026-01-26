package com.b2.e_commerce.repository;

import com.b2.e_commerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);
}
