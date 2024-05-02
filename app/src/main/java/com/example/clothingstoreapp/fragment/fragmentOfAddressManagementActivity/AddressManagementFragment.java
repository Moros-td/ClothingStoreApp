package com.example.clothingstoreapp.fragment.fragmentOfAddressManagementActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AddAddressActivity;
import com.example.clothingstoreapp.activity.AddressManagementActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.example.clothingstoreapp.adapter.AddressAdapter;
import com.example.clothingstoreapp.adapter.AddressManagementAdapter;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.custom_interface.IClickItemAddress;
import com.example.clothingstoreapp.custom_interface.IClickItemEditAddressListener;
import com.example.clothingstoreapp.entity.AddressEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.BooleanResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressManagementFragment extends Fragment {
    private RecyclerView recyclerView;
    private View mView;
    private Button btnOrder;
    private List<AddressEntity> list;
    private AddressManagementActivity addressManagementActivity;
    private AddressManagementAdapter addressManagementAdapter;
    private IClickItemEditAddressListener iClickItemEditAddressListener;
    SessionManager sessionManager;
    Dialog dialog;
    public AddressManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_address_management, container, false);
        addressManagementActivity = (AddressManagementActivity) getContext();

        recyclerView = mView.findViewById(R.id.rcv_address_management);
        sessionManager = new SessionManager(addressManagementActivity);
        callApiGetAllAddresses();

        LinearLayout layout = mView.findViewById(R.id.layout_add_address_mg); // ánh xạ layout từ layout của Fragment
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressManagementActivity.openFragment(AddressManagementActivity.FRAGMENT_ADDRESS_INFO,null);
            }
        });

        return mView;
    }

    private void callApiGetAllAddresses() {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            String email = sessionManager.getCustom("email");
            dialog = BaseActivity.openLoadingDialog(addressManagementActivity);
            ApiService.apiService.LoadAllAddress(token, email)
                    .enqueue(new Callback<List<AddressEntity>>() {
                        @Override
                        public void onResponse(Call<List<AddressEntity>> call, Response<List<AddressEntity>> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            list = response.body();

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(addressManagementActivity);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            addressManagementAdapter = new AddressManagementAdapter(list, new IClickItemEditAddressListener() {
                                @Override
                                public void onClickEditAddress(AddressEntity address) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("address", address);
                                    addressManagementActivity.openFragment(AddressManagementActivity.FRAGMENT_ADDRESS_INFO,bundle);
                                }

                                @Override
                                public void onClickSetDefaultAddress(AddressEntity address) {
                                    callApiSetDefaultAddress(address);
                                }

                                @Override
                                public void onClickDeleteAddress(AddressEntity address) {
                                    if(address.getIs_default() == 1){
                                        BaseActivity.openErrorDialog(addressManagementActivity, "Không thể xóa địa chỉ mặc định!");
                                    }
                                    else{
                                        callApiDeleteAddress(address);
                                    }
                                }
                            });

                            recyclerView.setAdapter(addressManagementAdapter);
                        }

                        @Override
                        public void onFailure(Call<List<AddressEntity>> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), "Không thể truy cập api");
                        }
                    });
        }
    }

    private void callApiDeleteAddress(AddressEntity address) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            String email = sessionManager.getCustom("email");
            dialog = BaseActivity.openLoadingDialog(addressManagementActivity);
            ApiService.apiService.DeleteAddress(token, email, address.getAddress_id())
                    .enqueue(new Callback<BooleanResponse>() {
                        @Override
                        public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BooleanResponse booleanResponse = response.body();
                            if(booleanResponse != null){
                                if("done".equals(booleanResponse.getSuccess())){
                                    BaseActivity.openSuccessDialog(addressManagementActivity, "Xóa địa chỉ thành công!");
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            callApiGetAllAddresses();
                                        }
                                    }, 2000);
                                }
                                else{
                                    BaseActivity.openErrorDialog(addressManagementActivity, "Xóa địa chỉ thất bại!");
                                }
                            }
                            else{
                                BaseActivity.openErrorDialog(addressManagementActivity, "Lỗi");
                            }
                        }

                        @Override
                        public void onFailure(Call<BooleanResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), "Không thể truy cập api");
                        }
                    });
        }
    }

    private void callApiSetDefaultAddress(AddressEntity address) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            String email = sessionManager.getCustom("email");
            dialog = BaseActivity.openLoadingDialog(addressManagementActivity);
            ApiService.apiService.SetAddressDefault(token, email, address.getAddress_id())
                    .enqueue(new Callback<BooleanResponse>() {
                        @Override
                        public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BooleanResponse booleanResponse = response.body();
                            if(booleanResponse != null){
                                if("done".equals(booleanResponse.getSuccess())){
                                    BaseActivity.openSuccessDialog(addressManagementActivity, "Đặt làm mặc định thành công!");
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            callApiGetAllAddresses();
                                        }
                                    }, 2000);
                                }
                                else{
                                    BaseActivity.openErrorDialog(addressManagementActivity, "Đặt làm mặc định thất bại!");
                                }
                            }
                            else{
                                BaseActivity.openErrorDialog(addressManagementActivity, "Lỗi");
                            }

                        }

                        @Override
                        public void onFailure(Call<BooleanResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), "Không thể truy cập api");
                        }
                    });
        }
    }

}