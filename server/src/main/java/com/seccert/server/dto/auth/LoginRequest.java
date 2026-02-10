package com.seccert.server.dto.auth;

public class LoginRequest {
    private String identifier; // email or username
    private String password;

    // getters/setters
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
