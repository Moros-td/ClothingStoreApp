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
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AuthenticationActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {

    Button submitBtn;
    EditText emailEditText;
    AuthenticationActivity authenticationActivity;
    Dialog dialog;
    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        authenticationActivity = (AuthenticationActivity) getContext();
        submitBtn = mView.findViewById(R.id.submitBtn);
        emailEditText = mView.findViewById(R.id.emailEditText);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                if(email.isEmpty()){
                    emailEditText.setError("Vui lòng email");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Email không hợp lệ");
                    return;
                }
                dialog = BaseActivity.openLoadingDialog(authenticationActivity);
                callApiForgotPassword(email);
            }
        });
        return mView;
    }

    private void callApiForgotPassword(String email) {
        ApiService.apiService.forgotPassword(email)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        LoginResponse loginResponse = response.body();
                        if(loginResponse != null){
                            if(loginResponse.getErr() != null)
                                BaseActivity.openErrorDialog(getContext(), loginResponse.getErr());
                            else if (loginResponse.getMessage() != null){
                                BaseActivity.openSuccessDialog(authenticationActivity, loginResponse.getMessage());
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        authenticationActivity.popVerifyFragment();
                                    }
                                }, 3000); // 3000 milliseconds = 3 second
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


}