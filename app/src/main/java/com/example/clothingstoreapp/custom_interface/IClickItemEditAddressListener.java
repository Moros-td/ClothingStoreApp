package com.example.clothingstoreapp.custom_interface;

import com.example.clothingstoreapp.entity.AddressEntity;

public interface IClickItemEditAddressListener {
    void onClickEditAddress(AddressEntity address);
    void onClickSetDefaultAddress(AddressEntity address);
    void onClickDeleteAddress(AddressEntity address);
}
