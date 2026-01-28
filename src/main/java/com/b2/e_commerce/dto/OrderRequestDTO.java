package com.b2.e_commerce.dto;

import com.b2.e_commerce.entity.enums.OrderStatus;

import jakarta.validation.constraints.*;

public class OrderRequestDTO {

    @NotNull(message = "Le statut de la commande est obligatoire")
    private OrderStatus status;

    public OrderRequestDTO() {}

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
