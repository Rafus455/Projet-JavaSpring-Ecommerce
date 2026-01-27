package com.b2.e_commerce.dto;

public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String firstname;
    // GETTERS

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // SETTERS

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
