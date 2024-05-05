package com.example.clothingstoreapp.fragment.fragmentOfOrdermanagementActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AddCommentActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.example.clothingstoreapp.activity.CommentActivity;
import com.example.clothingstoreapp.activity.OrderManagementActivity;
import com.example.clothingstoreapp.adapter.CartAdapter;
import com.example.clothingstoreapp.adapter.OrderAdapter;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.custom_interface.IClickItemOrderListener;
import com.example.clothingstoreapp.entity.CartItemEnity;
import com.example.clothingstoreapp.entity.OrderEntity;
import com.example.clothingstoreapp.entity.OrderItemEntity;
import com.example.clothingstoreapp.entity.ProductEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderManagementFragment extends Fragment {

    private RecyclerView recyclerView;

    private OrderManagementActivity orderManagementActivity;
    private List<OrderEntity> list;
    Dialog dialog;
    SessionManager sessionManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_order_management, container, false);
        recyclerView = mView.findViewById(R.id.rcv_order);

        orderManagementActivity = (OrderManagementActivity) getContext();
        sessionManager = new SessionManager(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(orderManagementActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        callApiGetAllOrders();
        return mView;
    }
    public void replaceFragmentAndMoveData(OrderEntity orderEntity){
        Bundle bundle = new Bundle();
        bundle.putSerializable("order_entity", orderEntity);
        orderManagementActivity.openFragment(OrderManagementActivity.FRAGMENT_ORDER_DETAIL, bundle);
    }

    private void callApiGetAllOrders() {

        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            String email = sessionManager.getCustom("email");
            dialog = BaseActivity.openLoadingDialog(getContext());

            ApiService.apiService.getAllOrders(token, email)
                    .enqueue(new Callback<List<OrderEntity>>() {
                        @Override
                        public void onResponse(Call<List<OrderEntity>> call, Response<List<OrderEntity>> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            list = response.body();
                            OrderAdapter orderAdapter = new OrderAdapter(list, new IClickItemOrderListener() {
                                @Override
                                public void onClickOrder(OrderEntity orderEntity) {
                                    replaceFragmentAndMoveData(orderEntity);
                                }
                            });
                            recyclerView.setAdapter(orderAdapter);
                        }

                        @Override
                        public void onFailure(Call<List<OrderEntity>> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), throwable.getMessage());
                        }
                    });
        }
    }
}