package com.example.clothingstoreapp.custom_interface;

import com.example.clothingstoreapp.entity.OrderEntity;
import com.example.clothingstoreapp.entity.ProductEntity;

public interface IClickItemOrderListener {
    void onClickOrder(OrderEntity orderEntity);
}
