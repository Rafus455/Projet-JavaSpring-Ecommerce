package com.b2.e_commerce.entity;

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

    private int stock;

    private int onSale;
    private String pathImage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // GETTERS

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int isOnSale() {
        return onSale;
    }

    public int getOnSale() {
        return onSale;
    }

    public String getPathImage() {
        return pathImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setOnSale(int onSale) {
        this.onSale = onSale;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
