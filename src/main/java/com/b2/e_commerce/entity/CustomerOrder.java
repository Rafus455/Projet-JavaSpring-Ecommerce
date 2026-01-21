package com.b2.e_commerce.entity;

import com.b2.e_commerce.entity.enums.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    private double priceTotal;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderDate;

    // GETTERS

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
