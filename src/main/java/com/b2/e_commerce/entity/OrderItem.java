package com.b2.e_commerce.entity;

import jakarta.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_order")
    private CustomerOrder order;

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Product product;

    private double unitPrice;
    private int quantity;

    // GETTERS

    public Long getId() {
        return id;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
