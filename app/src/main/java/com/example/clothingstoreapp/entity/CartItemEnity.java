package com.example.clothingstoreapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartItemEnity implements Serializable {
    @SerializedName("cart_code")
    private String codeCart;
    @SerializedName("product")
    private ProductEntity product;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("size")
    private String size;
    @SerializedName("total_price")
    private Double totalPrice;

    public String getCodeCart() {
        return codeCart;
    }

    public void setCodeCart(String codeCart) {
        this.codeCart = codeCart;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
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

    public CartItemEnity(String codeCart, ProductEntity product, int quantity, String size, Double totalPrice) {
        this.codeCart = codeCart;
        this.product = product;
        this.quantity = quantity;
        this.size = size;
        this.totalPrice = totalPrice;
    }
}