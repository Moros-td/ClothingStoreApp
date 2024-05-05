package com.example.clothingstoreapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CommentEntity implements Serializable {
    @SerializedName("comment_id")
    private int commentId;
    private String email;
    @SerializedName("product_code")
    private String productCode;
    private String comment;
    private double rating;
    @SerializedName("created_at")
    private String createdAt;

    public CommentEntity() {
    }

    public CommentEntity(int commentId, String email, String productCode, String comment, double rating, String createdAt) {
        this.commentId = commentId;
        this.email = email;
        this.productCode = productCode;
        this.comment = comment;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
