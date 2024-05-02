package com.example.clothingstoreapp.fragment.fragmentOfOrdermanagementActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.OrderManagementActivity;
import com.example.clothingstoreapp.activity.ProductDetailActivity;
import com.example.clothingstoreapp.adapter.OrderAdapter;
import com.example.clothingstoreapp.adapter.OrderItemFullInfoAdapter;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.custom_interface.IClickOrderItemListener;
import com.example.clothingstoreapp.entity.OrderEntity;
import com.example.clothingstoreapp.entity.OrderItemEntity;
import com.example.clothingstoreapp.entity.ProductEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.LoginResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailFragment extends Fragment {

    private RecyclerView recyclerView;

    private OrderManagementActivity orderManagementActivity;
    private List<OrderItemEntity> list;

    TextView fullNameTextView, phoneTextView, addressTextView,
            totalPriceTextView, paymentDateTextView, paymentCodeTextView;
    Button cancelButton;
    SessionManager sessionManager;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        fullNameTextView = mView.findViewById(R.id.fullNameTextView);
        phoneTextView = mView.findViewById(R.id.phoneTextView);
        addressTextView = mView.findViewById(R.id.addressTextView);
        totalPriceTextView = mView.findViewById(R.id.totalPriceTextView);
        cancelButton = mView.findViewById(R.id.cancelButton);
        paymentCodeTextView = mView.findViewById(R.id.paymentCodeTextView);
        paymentDateTextView = mView.findViewById(R.id.paymentDateTextView);
        sessionManager = new SessionManager(getContext());

        Bundle bundle = getArguments();
        if(bundle != null) {
            OrderEntity orderEntity = (OrderEntity) bundle.get("order_entity"); // Thay "key" bằng key thực tế bạn đã sử dụng trong Fragment A
            list = orderEntity.getListOrderItem();

            recyclerView = mView.findViewById(R.id.rcv_order_item);
            orderManagementActivity = (OrderManagementActivity) getContext();

            fullNameTextView.setText(orderEntity.getCustomer().getFullName());
            phoneTextView.setText(orderEntity.getCustomer().getPhone());
            addressTextView.setText(orderEntity.getAddress());
            totalPriceTextView.setText(String.valueOf(orderEntity.getTotalPrice()));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(orderManagementActivity);
            recyclerView.setLayoutManager(linearLayoutManager);

            OrderItemFullInfoAdapter orderItemFullInfoAdapter = new OrderItemFullInfoAdapter(list, new IClickOrderItemListener() {
                @Override
                public void onClickItemOrder(ProductEntity product) {
                    moveToDetailProductActivity(product);
                }
            });
            recyclerView.setAdapter(orderItemFullInfoAdapter);

            if(orderEntity.getPaymentCode() != null){
                paymentCodeTextView.setText(orderEntity.getPaymentCode());
                paymentDateTextView.setText(String.valueOf(orderEntity.getPaymentDate()));
            }else{
                paymentCodeTextView.setText("Chưa thanh toán");
                paymentDateTextView.setText("Chưa thanh toán");
            }

            if("pending".equals(orderEntity.getOrderState())){

                cancelButton.setVisibility(View.VISIBLE);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callApiCancelOrder(orderEntity.getOrderCode());
                    }
                });
            }
            else{
                cancelButton.setVisibility(View.GONE);
            }
        }

        return mView;
    }

    private void moveToDetailProductActivity(ProductEntity product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void callApiCancelOrder(String orderCode) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            dialog = BaseActivity.openLoadingDialog(getContext());
            ApiService.apiService.cancelOrder(token, orderCode)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            LoginResponse response1 = response.body();
                            if(response1 != null){
                                if("done".equals(response1.getMessage())){
                                    BaseActivity.openSuccessDialog(getContext(), "Hủy đơn thành công!");

                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            orderManagementActivity.popFragmentOrderDetail();
                                        }
                                    }, 2000);
                                }
                                else{
                                    BaseActivity.openErrorDialog(getContext(), response1.getErr());
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), throwable.getMessage());
                        }
                    });
        }
    }

}