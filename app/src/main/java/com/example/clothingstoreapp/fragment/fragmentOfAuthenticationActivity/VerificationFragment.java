package com.example.clothingstoreapp.fragment.fragmentOfAuthenticationActivity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AuthenticationActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationFragment extends Fragment {
    Button verifyBtn;
    AuthenticationActivity authenticationActivity;
    EditText otpEditText;

    SessionManager sessionManager;
    Dialog dialog;
    public VerificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_verification, container, false);

        verifyBtn = mView.findViewById(R.id.verifyBtn);
        otpEditText = mView.findViewById(R.id.otpEditText);
        authenticationActivity = (AuthenticationActivity) getContext();
        sessionManager = new SessionManager(authenticationActivity);
        //otpEditText.setText(sessionManager.getCustom("verify_token"));
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifyCode = otpEditText.getText().toString();
                if(verifyCode.isEmpty()){
                    otpEditText.setError("Không được để trống");
                    return;
                }

                String token = sessionManager.getCustom("verify_token");
                dialog = BaseActivity.openLoadingDialog(authenticationActivity);
                ApiService.apiService.verify(verifyCode, token)
                                .enqueue(new Callback<LoginResponse>() {
                                    @Override
                                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        LoginResponse loginResponse = response.body();
                                        if(loginResponse != null){
                                            if(loginResponse.getErr() != null) {
                                                if(loginResponse.getErr().equals("Token expired") ||
                                                        loginResponse.getErr().equals("Incorrectly more than 3 times")){
                                                    sessionManager.deleteCustom("verify_token");

                                                    BaseActivity.openErrorDialog(authenticationActivity, "Đã sai mã xác nhận quá 3 lần, vui lòng đăng kí lại");
                                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            authenticationActivity.popVerifyFragment();
                                                        }
                                                    }, 3000); // 3000 milliseconds = 3 second
                                                }
                                                else {
                                                    if(loginResponse.getErr().equals("Wrong verify code")) {
                                                        otpEditText.setText("");
                                                        sessionManager.saveCustom("verify_token", loginResponse.getToken());
                                                        BaseActivity.openErrorDialog(getContext(), "Sai mã xác nhận");
                                                    }
                                                    else{
                                                        BaseActivity.openErrorDialog(getContext(), "Lỗi");
                                                    }
                                                }
                                            }
                                            else if (loginResponse.getMessage() != null){
                                                if(loginResponse.getMessage().equals("done")){
                                                    BaseActivity.openSuccessDialog(authenticationActivity, "Đăng kí thành công, vui lòng đăng nhập");

                                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            authenticationActivity.finish();
                                                        }
                                                    }, 3000); // 3000 milliseconds = 3 second
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        BaseActivity.openErrorDialog(getContext(), "Không thể truy cập api");
                                    }
                                });
            }
        });

        return mView;
    }
}