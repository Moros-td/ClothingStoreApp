package com.example.clothingstoreapp.fragment.fragmentOfAccountInfoActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AccountInfoActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.entity.CustomerEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInfoFragment extends Fragment {
    Button updateInfoBtn, changePasswordBtn;
    AccountInfoActivity accountInfoActivity;

    EditText emailEditText, phoneEditText, fullNameEditText;
    SessionManager sessionManager;

    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =  inflater.inflate(R.layout.fragment_account_info, container, false);
        accountInfoActivity = (AccountInfoActivity) getContext();
        updateInfoBtn = mView.findViewById(R.id.updateInfoBtn);
        changePasswordBtn = mView.findViewById(R.id.changePasswordBtn);

        emailEditText = mView.findViewById(R.id.emailEditText);
        phoneEditText = mView.findViewById(R.id.phoneEditText);
        fullNameEditText = mView.findViewById(R.id.fullNameEditText);
        sessionManager = new SessionManager(accountInfoActivity);
        final Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);

        callApiGetCustomerInfo();
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                accountInfoActivity.openFragment(AccountInfoActivity.FRAGMENT_PASSWORD_CHANGE);
            }
        });
        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);

                String email = emailEditText.getText().toString();
                String fullName = fullNameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                boolean check = true;
                // Kiểm tra định dạng email
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Email không hợp lệ");
                    check = false;
                }

                if (phone.isEmpty()) {
                    phoneEditText.setError("Không được để trống số điện thoại");
                    check = false;
                }

                if (fullName.isEmpty()) {
                    fullNameEditText.setError("Không được để trống họ tên");
                    check = false;
                }

                if(!check)
                    return;


                if (checkPhone(phone)) {
                    phoneEditText.setError("Số điện thoại không hợp lệ");
                    return;
                }
                callApiChangeInfo(email, fullName, phone);
            }
        });
        return mView;
    }
    private void callApiGetCustomerInfo() {
        if(sessionManager.isLoggedIn()){
            dialog = BaseActivity.openLoadingDialog(accountInfoActivity);
            String token = sessionManager.getJwt();
            String email = sessionManager.getCustom("email");
            ApiService.apiService.getCustomerInfo(token, email)
                    .enqueue(new Callback<CustomerEntity>() {
                        @Override
                        public void onResponse(Call<CustomerEntity> call, Response<CustomerEntity> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            CustomerEntity customerEntity = response.body();
                            emailEditText.setText(customerEntity.getEmail());
                            phoneEditText.setText(customerEntity.getPhone());
                            fullNameEditText.setText(customerEntity.getFullName());
                        }

                        @Override
                        public void onFailure(Call<CustomerEntity> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
                        }
                    });
        }
    }

    private boolean checkPhone(String phone) {

        if(!phone.startsWith("0")){
            return true;
        }
        if(phone.length() < 10){
            return true;
        }

        return false;
    }

    private void callApiChangeInfo(String email,String fullName,String phone) {
        if(sessionManager.isLoggedIn()) {
            dialog = BaseActivity.openLoadingDialog(accountInfoActivity);
            String token = sessionManager.getJwt();

            ApiService.apiService.updateCustomerInfo(token, email, fullName, phone)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if(response.body() != null){
                                LoginResponse response1 = response.body();
                                if(response1.getMessage().equals("done")){
                                    BaseActivity.openSuccessDialog(getContext(), "Thay đổi thông tin thành công!");
                                }
                                else{
                                    BaseActivity.openErrorDialog(getContext(), "Có lỗi xảy ra trong khi thay đổi thông tin!");
                                }
                            }
                            else{
                                BaseActivity.openErrorDialog(getContext(), "Lỗi");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
                        }
                    });
        }
    }

}