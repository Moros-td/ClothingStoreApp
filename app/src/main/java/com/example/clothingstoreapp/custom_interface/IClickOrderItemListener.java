package com.example.clothingstoreapp.custom_interface;

import com.example.clothingstoreapp.entity.OrderItemEntity;
import com.example.clothingstoreapp.entity.ProductEntity;

public interface IClickOrderItemListener {
    void onClickItemOrder(ProductEntity product);
    void onClickBtnComment(OrderItemEntity orderItemEntity);
}
