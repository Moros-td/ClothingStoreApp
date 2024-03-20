package com.example.clothingstoreapp.adapter.categorylistview;

public class ItemModel {
    public static final int PARENT_TYPE = 0;
    public static final int CHILD_TYPE = 1;

    private String categoryname;
//    private String childName;
    private int mType;

    public ItemModel(String categoryname, int mType) {
        this.categoryname=categoryname;

        this.mType = mType;
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
}
