package com.example.clothingstoreapp.custom_interface;

import com.example.clothingstoreapp.entity.CartItemEnity;

public interface IClickItemCartListener {
    void onClickRemove(CartItemEnity cartItem);

    void onClickSubtract(CartItemEnity cartItem);

    void onClickAdd(CartItemEnity cartItem);
}
