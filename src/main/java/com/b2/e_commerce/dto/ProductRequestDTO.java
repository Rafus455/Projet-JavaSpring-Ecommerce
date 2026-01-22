package com.b2.e_commerce.dto;

import jakarta.validation.constraints.*;

public class ProductRequestDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @Positive(message = "Le prix doit être supérieur à 0")
    private double price;

    @NotNull(message = "Le stock est obligatoire")
    private int stock;

    @NotNull(message = "Le champ onSale est obligatoire")
    private int onSale;

    @NotBlank(message = "Le chemin de l'image est obligatoire")
    private String pathImage;
    
    @NotNull(message = "La catégorie est obligatoire")
    private Long categoryId;

    public ProductRequestDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getOnSale() {
        return onSale;
    }

    public void setOnSale(int onSale) {
        this.onSale = onSale;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
