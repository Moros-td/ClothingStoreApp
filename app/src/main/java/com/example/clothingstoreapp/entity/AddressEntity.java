package com.example.clothingstoreapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressEntity implements Serializable {
    @SerializedName("full_name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("address")
    private String address;
    @SerializedName("is_default")
    private int is_default;
    @SerializedName("email")
    private String email;
    @SerializedName("address_id")
    private int address_id;


    public AddressEntity(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }
}
