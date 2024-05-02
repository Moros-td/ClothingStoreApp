package com.example.clothingstoreapp.fragment.fragmentOfAccountInfoActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
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
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordChangeFragment extends Fragment {

    Button submitBtn;

    EditText currentPasswordEditText, newPasswordEditText, retypeNewPasswordEditText;
    SessionManager sessionManager;
    Dialog dialog;

    AccountInfoActivity accountInfoActivity;
    public PasswordChangeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_password_change, container, false);
        submitBtn = mView.findViewById(R.id.submitBtn);
        accountInfoActivity = (AccountInfoActivity) getContext();
        currentPasswordEditText = mView.findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = mView.findViewById(R.id.newPasswordEditText);
        retypeNewPasswordEditText = mView.findViewById(R.id.retypeNewPasswordEditText);
        sessionManager = new SessionManager(getContext());
        final Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);

                String currentPass = currentPasswordEditText.getText().toString();
                String newPass = newPasswordEditText.getText().toString();
                String retypeNewPass = retypeNewPasswordEditText.getText().toString();

                boolean check = true;

                // Kiểm tra mật khẩu
                if (currentPass.isEmpty()) {
                    currentPasswordEditText.setError("Không được bỏ trống password");
                    check = false;
                }

                if (newPass.isEmpty()) {
                    newPasswordEditText.setError("Không được bỏ trống password mới");
                    check = false;
                }

                if (retypeNewPass.isEmpty()) {
                    retypeNewPasswordEditText.setError("Yêu cầu nhập lại password mới");
                    check = false;
                }

                if(!check)
                    return;

                // Kiểm tra mật khẩu nhập lại có trùng khớp với mật khẩu đã nhập không
                if (!newPass.equals(retypeNewPass)) {
                    retypeNewPasswordEditText.setError("Mật khẩu mới nhập lại không khớp");
                    return;
                }

                if (newPass.equals(currentPass)) {
                    newPasswordEditText.setError("Mật khẩu mới phải khác với mật khẩu cũ");
                    return;
                }

                callApiUpdatePassword(currentPass, newPass);
            }
        });
        return mView;
    }

    private void callApiUpdatePassword(String currentPass, String newPass) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            String email = sessionManager.getCustom("email");
            dialog = BaseActivity.openLoadingDialog(getContext());

            ApiService.apiService.updateCustomerPassword(token, email, currentPass, newPass)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if(response.body() != null){
                                LoginResponse response1 = response.body();
                                if("done".equals(response1.getMessage())){
                                    BaseActivity.openSuccessDialog(getContext(), "Thay đổi mật khẩu thành công!");
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            accountInfoActivity.popFragmentChangePassword();
                                        }
                                    }, 2000);
                                }
                                else {
                                    currentPasswordEditText.setError(response1.getErr());
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
                            BaseActivity.openErrorDialog(getContext(), "Không thể truy cập api");
                        }
                    });
        }
    }

}