package com.feedback.poc.loginservice.domain;

public class UserCredential {

    private String username;
    private CharSequence password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CharSequence getPassword() {
        return password;
    }

    public void setPassword(CharSequence password) {
        this.password = password;
    }
}
