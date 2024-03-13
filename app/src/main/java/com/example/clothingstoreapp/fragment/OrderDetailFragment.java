package com.example.clothingstoreapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.OrderManagementActivity;
import com.example.clothingstoreapp.adapter.OrderAdapter;
import com.example.clothingstoreapp.adapter.OrderItemFullInfoAdapter;
import com.example.clothingstoreapp.entity.OrderEntity;
import com.example.clothingstoreapp.entity.OrderItemEntity;
import com.example.clothingstoreapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailFragment extends Fragment {

    private RecyclerView recyclerView;

    private OrderManagementActivity orderManagementActivity;
    private List<OrderItemEntity> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_order_detail, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            OrderEntity orderEntity = (OrderEntity) bundle.get("order_entity"); // Thay "key" bằng key thực tế bạn đã sử dụng trong Fragment A
            Toast.makeText(getContext(), orderEntity.getOrderCode(), Toast.LENGTH_LONG).show();
        }
        recyclerView = mView.findViewById(R.id.rcv_order_item);
        orderManagementActivity = (OrderManagementActivity) getContext();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(orderManagementActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        list = getListOrderItem();

        OrderItemFullInfoAdapter orderItemFullInfoAdapter = new OrderItemFullInfoAdapter(list);
        recyclerView.setAdapter(orderItemFullInfoAdapter);

        return mView;
    }

    private List<OrderItemEntity> getListOrderItem() {
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

        ProductEntity product3 = new ProductEntity("SP789", "ÁO SƠ MI NAM", 50, 500.00);

        OrderItemEntity orderItemEntity3 = new OrderItemEntity();
        orderItemEntity3.setProduct(product3);
        orderItemEntity3.setOrderItemId(2);
        orderItemEntity3.setOrderCode("DH001");
        orderItemEntity3.setQuantity(2);
        orderItemEntity3.setTotalPrice(10000.02);
        orderItemEntity3.setSize("S");

        listOrderItem.add(orderItemEntity2);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setListOrderItem(listOrderItem);
        orderEntity.setOrderCode("DH001");
        orderEntity.setOrderDate(new Date());
        orderEntity.setTotalPrice(10000.02);
        orderEntity.setOrderState("Đã giao");

        return listOrderItem;
    }
}