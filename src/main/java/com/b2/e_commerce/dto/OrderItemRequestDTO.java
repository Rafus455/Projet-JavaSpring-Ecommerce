package com.b2.e_commerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemRequestDTO {

    @NotNull(message = "L'id de la commande est obligatoire")
    private Long orderId;

    @NotNull(message = "L'id du produit est obligatoire")
    private Long productId;

    @Positive(message = "La quantité doit être supérieure à 0")
    private int quantity;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
