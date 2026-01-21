package com.b2.e_commerce.repository;

import com.b2.e_commerce.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
}
