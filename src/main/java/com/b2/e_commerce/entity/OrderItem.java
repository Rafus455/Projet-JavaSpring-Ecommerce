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
}
