package com.example.clothingstoreapp.entity;

import java.io.Serializable;

public class ProductEntity implements Serializable {
    private String productCode;
    private String productName;
    private String productColor;
    private int productQuantity;
    private Double productPrice;

    public ProductEntity(String productCode, String productName, int productQuantity, Double productPrice) {
        this.productCode = productCode;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
}
