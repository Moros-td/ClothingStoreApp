package com.example.clothingstoreapp.adapter.categorylistview;

import com.example.clothingstoreapp.entity.CategoryEntity;

public class ItemModel {
    public static final int PARENT_TYPE = 0;
    public static final int CHILD_TYPE = 1;

    private String categoryname;
//    private String childName;
    private int mType;
    private CategoryEntity category;

    public ItemModel(String categoryname, int mType, CategoryEntity category) {
        this.categoryname=categoryname;
        this.mType = mType;
        this.category = category;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
