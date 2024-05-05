package com.example.clothingstoreapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderItemEntity implements Serializable {
    @SerializedName("order_item_id")
    private int orderItemId;
    @SerializedName("order_code")
    private String orderCode;

    private ProductEntity product;
    private int quantity;
    private String size;
    @SerializedName("total_price")
    private Double totalPrice;
    private int commentState;

    public int getCommentState() {
        return commentState;
    }

    public void setCommentState(int commentState) {
        this.commentState = commentState;
    }

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
