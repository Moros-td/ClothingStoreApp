package com.example.clothingstoreapp.entity;

public class Photo {
    private int resourceID;
    private String path;

    public Photo(String path) {
        this.path = path;
    }

    public Photo(int resourceID) {
        this.resourceID = resourceID;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

