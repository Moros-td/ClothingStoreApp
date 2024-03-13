package com.example.clothingstoreapp.entity;

import java.io.Serializable;

public class CategoryEntity implements Serializable {

    private int id;
    private String categoryName;
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
