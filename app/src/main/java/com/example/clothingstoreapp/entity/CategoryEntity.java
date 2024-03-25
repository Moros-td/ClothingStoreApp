package com.example.clothingstoreapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryEntity implements Serializable {

    @SerializedName("category_id")
    private int id;
    @SerializedName("name")
    private String categoryName;
    @SerializedName("parent_category")
    private CategoryEntity categoryParent;

    public CategoryEntity(int id, String categoryName, CategoryEntity categoryParent) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryParent = categoryParent;
    }

    public CategoryEntity getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(CategoryEntity categoryParent) {
        this.categoryParent = categoryParent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
