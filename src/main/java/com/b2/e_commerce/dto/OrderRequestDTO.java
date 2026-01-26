package com.b2.e_commerce.dto;

import com.b2.e_commerce.entity.enums.OrderStatus;

import jakarta.validation.constraints.*;

public class OrderRequestDTO {
	@NotNull(message = "Id du user est obligatoire")
    private Long userId;

    @Positive(message = "Le prix total doit être supérieur à 0")
    private double priceTotal;

    @NotNull(message = "Le statut de la commande est obligatoire")
    private OrderStatus status;

    public OrderRequestDTO() {}
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
