package com.example.clothingstoreapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.example.clothingstoreapp.activity.OrderManagementActivity;
import com.example.clothingstoreapp.adapter.CartAdapter;
import com.example.clothingstoreapp.adapter.OrderAdapter;
import com.example.clothingstoreapp.custom_interface.IClickItemOrderListener;
import com.example.clothingstoreapp.entity.CartItemEnity;
import com.example.clothingstoreapp.entity.OrderEntity;
import com.example.clothingstoreapp.entity.OrderItemEntity;
import com.example.clothingstoreapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderManagementFragment extends Fragment {

    private RecyclerView recyclerView;

    private OrderManagementActivity orderManagementActivity;
    private List<OrderEntity> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_order_management, container, false);
        recyclerView = mView.findViewById(R.id.rcv_order);

        orderManagementActivity = (OrderManagementActivity) getContext();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(orderManagementActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        list = getListOrder();
        OrderAdapter orderAdapter = new OrderAdapter(list, new IClickItemOrderListener() {
            @Override
            public void onClickOrder(OrderEntity orderEntity) {
                replaceFragmentAndMoveData(orderEntity);
            }
        });
        recyclerView.setAdapter(orderAdapter);
        return mView;
    }

    public void replaceFragmentAndMoveData(OrderEntity orderEntity){
        Bundle bundle = new Bundle();
        bundle.putSerializable("order_entity", orderEntity);
        orderManagementActivity.openFragment(OrderManagementActivity.FRAGMENT_ORDER_DETAIL, bundle);
    }

    private List<OrderEntity> getListOrder() {
        List<OrderEntity> list = new ArrayList<>();
        List<OrderItemEntity> listOrderItem = new ArrayList<>();
        ProductEntity product = new ProductEntity("SP123", "ÁO THUN TRƠN CỔ ĐỨC KHUY NGỌC TRAI ", 50, 500.00);

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setProduct(product);
        orderItemEntity.setOrderItemId(1);
        orderItemEntity.setOrderCode("DH001");
        orderItemEntity.setQuantity(2);
        orderItemEntity.setTotalPrice(10000.02);
        orderItemEntity.setSize("S");

        listOrderItem.add(orderItemEntity);

        ProductEntity product2 = new ProductEntity("SP456", "ÁO SƠ MI NỮ", 50, 500.00);

        OrderItemEntity orderItemEntity2 = new OrderItemEntity();
        orderItemEntity2.setProduct(product2);
        orderItemEntity2.setOrderItemId(2);
        orderItemEntity2.setOrderCode("DH001");
        orderItemEntity2.setQuantity(2);
        orderItemEntity2.setTotalPrice(10000.02);
        orderItemEntity2.setSize("S");

        listOrderItem.add(orderItemEntity2);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setListOrderItem(listOrderItem);
        orderEntity.setOrderCode("DH001");
        orderEntity.setOrderDate(new Date());
        orderEntity.setTotalPrice(10000.02);
        orderEntity.setOrderState("Đã giao");

        list.add(orderEntity);
        return list;
    }
}