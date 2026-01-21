package com.b2.e_commerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    private String name;
    private String firstname;
    private String mail;
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
