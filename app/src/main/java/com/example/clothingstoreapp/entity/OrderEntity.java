package com.example.clothingstoreapp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderEntity implements Serializable {
    private String orderCode;
    private String orderState;
    private Date orderDate;

    private Double totalPrice;

    private List<OrderItemEntity> listOrderItem;

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
