package com.b2.e_commerce.entity;

import jakarta.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
