package com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AddAddressActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.example.clothingstoreapp.adapter.AddressAdapter;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.custom_interface.IClickItemAddress;
import com.example.clothingstoreapp.entity.AddressEntity;
import com.example.clothingstoreapp.entity.CartItemEnity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.BooleanResponse;
import com.example.clothingstoreapp.response.OrderResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAddressFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartBaseActivity cartBaseActivity;
    private View mView;
    private Button btnOrder;
    private List<AddressEntity> list;
    private List<CartItemEnity> cartItemEnityList;
    private IClickItemAddress iClickItemAddress;
    private RadioButton radioButton;
    private AddressAdapter addressAdapter;
    private TextView totalPriceTextView, tempPriceTextView;
    private SessionManager sessionManager;
    private Dialog dialog;
    Double totalPrice;
    String addressResquest, orderCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_cart_address, container, false);

        cartBaseActivity = (CartBaseActivity) getActivity();
        sessionManager = new SessionManager(cartBaseActivity);
        recyclerView = mView.findViewById(R.id.rcv_address_cart);
        dialog = BaseActivity.openLoadingDialog(getContext());

        list = new ArrayList<>();
        callApiGetAllAddress();

        return mView;
    }

    private void callApiGetAllAddress() {
        callApiGetAllCart();
        // Ánh xạ TextView của footer
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Lấy dữ liệu từ Bundle
            String total = bundle.getString("totalPrice");
            String temp = bundle.getString("tempPrice");
            totalPrice = bundle.getDouble("total");

            totalPriceTextView = mView.findViewById(R.id.cart_totalPrice);
            totalPriceTextView.setText(total);

            tempPriceTextView = mView.findViewById(R.id.cart_tempPrice);
            tempPriceTextView.setText(temp);
        }
        ApiService.apiService.LoadAllAddress(sessionManager.getJwt(), sessionManager.getCustom("email")).enqueue(new Callback<List<AddressEntity>>() {
            @Override
            public void onResponse(Call<List<AddressEntity>> call, Response<List<AddressEntity>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cartBaseActivity);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    iClickItemAddress = new IClickItemAddress() {
                        @Override
                        public void onClickChoose(AddressEntity addressEntity) {
                            Dialog dg = BaseActivity.openLoadingDialog(getContext());
                            ApiService.apiService.SetAddressDefault(sessionManager.getJwt(), sessionManager.getCustom("email"), addressEntity.getAddress_id()).enqueue(new Callback<BooleanResponse>() {
                                @Override
                                public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {

                                    if (response.isSuccessful()) {
                                        BooleanResponse result = response.body();
                                        if ("done".equals(result.getSuccess())) {
                                            callApiGetAllAddress();
                                        }
                                    } else {
                                        BaseActivity.openErrorDialog(getContext(), "Xảy ra lỗi!");
                                    }
                                    dg.dismiss();
                                }

                                @Override
                                public void onFailure(Call<BooleanResponse> call, Throwable throwable) {
                                    if (dg != null && dg.isShowing()) {
                                        dg.dismiss();
                                    }
                                    BaseActivity.openErrorDialog(getContext(), "Vui lòng thử lại sau!");
                                }
                            });
                        }
                    };

                    addressAdapter = new AddressAdapter(list, iClickItemAddress);
                    recyclerView.setAdapter(addressAdapter);

                    //button chuyển trang
                    btnOrder = mView.findViewById(R.id.btn_address_cart);
                    btnOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            for (AddressEntity address : list) {
                                if (address.getIs_default() == 1) {
                                    addressResquest = address.getAddress();
                                }
                            }
                            ApiService.apiService.AddOrder(sessionManager.getJwt(), "pending", totalPrice,
                                    sessionManager.getCustom("email"), addressResquest).enqueue(new Callback<OrderResponse>() {
                                @Override
                                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                                    if (response.isSuccessful()) {
                                        OrderResponse result = response.body();
                                        if (result.getOrder_code() != null) {
                                            orderCode = result.getOrder_code();
                                            addOrderItemsSequentially(orderCode, cartItemEnityList, 0);
                                        }
                                        callApiDeleteAllCart();
                                    } else {
                                        BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
                                    }

                                }

                                @Override
                                public void onFailure(Call<OrderResponse> call, Throwable throwable) {
                                    BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
                                }
                            });
                        }
                    });
                    //Chuyển sang Activity AddAddress
                    LinearLayout layout = mView.findViewById(R.id.layout_add_address);
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Tạo một Intent để chuyển từ Fragment sang Activity mới
                            Intent intent = new Intent(getActivity(), AddAddressActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
                }
            }

            @Override
            public void onFailure(Call<List<AddressEntity>> call, Throwable throwable) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");

            }
        });

    }

    private void callApiDeleteAllCart() {
        Dialog dg = BaseActivity.openLoadingDialog(getContext());
        ApiService.apiService.DeleteAllItem(sessionManager.getJwt(), cartItemEnityList.get(0).getCodeCart()).enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                dg.dismiss();
                if (response.isSuccessful()) {
                    BooleanResponse result = response.body();
                    if ("done".equals(result.getSuccess())) {
                        Fragment fragment = new CartSuccessFragment();
                        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.containerCart, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } else {
                    BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
                }
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable throwable) {
                if (dg != null && dg.isShowing()) {
                    dg.dismiss();
                }
                BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
            }
        });
    }

    private void callApiGetAllCart() {
        ApiService.apiService.getAllCartItems(sessionManager.getJwt(), sessionManager.getCustom("email")).enqueue(new Callback<List<CartItemEnity>>() {
            @Override
            public void onResponse(Call<List<CartItemEnity>> call, Response<List<CartItemEnity>> response) {
                cartItemEnityList = response.body();
            }

            @Override
            public void onFailure(Call<List<CartItemEnity>> call, Throwable throwable) {
                BaseActivity.openErrorDialog(getContext(), "Xảy ra lỗi!");
            }
        });
    }
    private void addOrderItemsSequentially(final String orderCode, final List<CartItemEnity> items, final int index) {
        if (index >= items.size()) {
            // Nếu đã thêm tất cả các mục, gọi API để xóa tất cả các mục trong giỏ hàng
            callApiDeleteAllCart();
            return;
        }

        CartItemEnity item = items.get(index);
        callApiAddOrderItem(orderCode, item.getProduct().getProductCode(), item.getQuantity(), item.getSize(), item.getTotalPrice(), new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                BooleanResponse result = response.body();
                if ("done".equals(result.getSuccess())) {
                    // Nếu thêm mục thành công, thêm mục tiếp theo
                    addOrderItemsSequentially(orderCode, items, index + 1);
                } else {
                    BaseActivity.openErrorDialog(getContext(), "Xảy ra lỗi khi thêm mục vào đơn hàng!");
                }
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable throwable) {
                BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
            }
        });
    }

    private void callApiAddOrderItem(String orderCode, String productCode, int quantity, String size, Double totalPrice, Callback<BooleanResponse> callback) {
        ApiService.apiService.AddOrderItem(sessionManager.getJwt(), orderCode, productCode, quantity, size, totalPrice).enqueue(callback);
    }
}
