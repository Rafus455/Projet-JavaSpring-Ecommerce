package com.b2.e_commerce.DTO;

public class LoginResponse {

    private String token;

    public LoginResponse() {
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    // GETTER
    public String getToken() {
        return token;
    }

    // SETTER
    public void setToken(String token) {
        this.token = token;
    }
}
