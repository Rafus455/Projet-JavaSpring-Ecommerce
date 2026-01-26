package com.b2.e_commerce.dto;

import com.b2.e_commerce.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateDTO {

    @NotNull(message = "Le statut est obligatoire")
    private OrderStatus status;

    // getter + setter
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
  