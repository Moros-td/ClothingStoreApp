package com.example.clothingstoreapp.response;

public class OrderResponse {
    private String order_code;
    private String error;

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
