package com.example.doctorcare.infrastructure.security.service;

public class JwtCommand {

    private String token;

    public JwtCommand() {
    }

    public JwtCommand(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}