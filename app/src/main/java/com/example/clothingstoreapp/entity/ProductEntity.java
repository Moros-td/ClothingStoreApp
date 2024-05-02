package com.example.clothingstoreapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ProductEntity implements Serializable {
    @SerializedName("product_code")
    private String productCode;
    @SerializedName("name")
    private String productName;
    @SerializedName("color")
    private String productColor;
    @SerializedName("quantity")
    private int productQuantity;
    @SerializedName("price")
    private Double productPrice;

    private String description;

    private List<String> images;

    private Map<String, Integer> sizes;

    @SerializedName("update_latest")
    private String updateLatest;

    public ProductEntity() {
    }

    public ProductEntity(String productCode, String productName, int productQuantity, Double productPrice) {
        this.productCode = productCode;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    public ProductEntity(String productCode, String productName, String productColor, int productQuantity, Double productPrice, String description, List<String> images, Map<String, Integer> sizes, String updateLatest) {
        this.productCode = productCode;
        this.productName = productName;
        this.productColor = productColor;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.description = description;
        this.images = images;
        this.sizes = sizes;
        this.updateLatest = updateLatest;
    }

    public String getUpdateLatest() {
        return updateLatest;
    }

    public void setUpdateLatest(String updateLatest) {
        this.updateLatest = updateLatest;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Map<String, Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, Integer> sizes) {
        this.sizes = sizes;
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
