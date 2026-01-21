package com.b2.e_commerce.entity;

import com.b2.e_commerce.entity.enums.ProductStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    private String name;

    @Column(length = 1000)
    private String description;

    private double price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private boolean onSale;
    private String pathImage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
