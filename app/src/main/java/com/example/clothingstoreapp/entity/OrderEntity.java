package com.example.clothingstoreapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderEntity implements Serializable {
    @SerializedName("order_code")
    private String orderCode;
    @SerializedName("state")
    private String orderState;
    @SerializedName("order_date")
    private Date orderDate;
    @SerializedName("total_price")
    private Double totalPrice;

    @SerializedName("customer")
    private CustomerEntity customer;
    @SerializedName("address")
    private String address;
    @SerializedName("payment_code")
    private String paymentCode;
    @SerializedName("payment_date")
    private Date paymentDate;
    @SerializedName("order_items")
    private List<OrderItemEntity> listOrderItem;

    public OrderEntity() {
    }

    public OrderEntity(String orderCode, String orderState, Date orderDate, Double totalPrice, CustomerEntity customer, String address, String paymentCode, Date paymentDate, List<OrderItemEntity> listOrderItem) {
        this.orderCode = orderCode;
        this.orderState = orderState;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.customer = customer;
        this.address = address;
        this.paymentCode = paymentCode;
        this.paymentDate = paymentDate;
        this.listOrderItem = listOrderItem;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public List<OrderItemEntity> getListOrderItem() {
        return listOrderItem;
    }

    public void setListOrderItem(List<OrderItemEntity> listOrderItem) {
        this.listOrderItem = listOrderItem;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
