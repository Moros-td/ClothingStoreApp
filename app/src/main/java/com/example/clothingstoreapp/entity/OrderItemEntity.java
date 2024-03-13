package com.example.clothingstoreapp.entity;

import java.util.List;

public class OrderItemEntity {
    private int orderItemId;
    private String orderCode;

    private ProductEntity product;
    private int quantity;
    private String size;
    private Double totalPrice;

    public int getOrderItemId() {
        return orderItemId;
    }
    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
