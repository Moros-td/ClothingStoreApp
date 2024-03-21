package com.example.clothingstoreapp.response;

public class LoginResponse{
    private String token;
    private String err;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
