package com.b2.e_commerce.entity.enums;

public enum OrderStatus {
	DRAFT,
    PENDING,
    PAID,
    SHIPPED,
    CANCELLED;
    
    public boolean canTransitionTo(OrderStatus target) {
        return switch (this) {
            case DRAFT -> target == PENDING || target == CANCELLED;
            case PENDING -> target == PAID || target == CANCELLED;
            case PAID -> target == SHIPPED;
            case SHIPPED -> target == CANCELLED;
            default -> false;
        };
    }
}
