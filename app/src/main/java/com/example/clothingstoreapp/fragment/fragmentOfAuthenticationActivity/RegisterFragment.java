package com.example.clothingstoreapp.fragment.fragmentOfAuthenticationActivity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
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

public class RegisterFragment extends Fragment {

    Button registerBtn;
    TextView errorTextView;
    EditText emailEditText, passwordEditText, retypePasswordEditText, phoneEditText, nameEditText;
    AuthenticationActivity authenticationActivity;

    Dialog dialog;

    SessionManager sessionManager;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_register, container, false);
        authenticationActivity = (AuthenticationActivity) getContext();
        errorTextView = mView.findViewById(R.id.errorTextView);
        emailEditText = mView.findViewById(R.id.emailEditText);
        passwordEditText = mView.findViewById(R.id.passwordEditText);
        retypePasswordEditText = mView.findViewById(R.id.retypePasswordEditText);
        phoneEditText = mView.findViewById(R.id.phoneEditText);
        nameEditText = mView.findViewById(R.id.nameEditText);
        registerBtn = mView.findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String retypePassword = retypePasswordEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();

                boolean check = true;

                // Kiểm tra định dạng email
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Email không hợp lệ");
                    check = false;
                }

                // Kiểm tra mật khẩu
                if (password.isEmpty()) {
                    passwordEditText.setError("Không được bỏ trống password");
                    check = false;
                }

                if (retypePassword.isEmpty()) {
                    retypePasswordEditText.setError("Yêu cầu nhập lại password");
                    check = false;
                }

                if (phone.isEmpty()) {
                    phoneEditText.setError("Không được để trống số điện thoại");
                    check = false;
                }

                if (name.isEmpty()) {
                    nameEditText.setError("Không được để trống họ tên");
                    check = false;
                }

                if(!check)
                    return;

                // Kiểm tra mật khẩu nhập lại có trùng khớp với mật khẩu đã nhập không
                if (!password.equals(retypePassword)) {
                    retypePasswordEditText.setError("Mật khẩu nhập lại không khớp");
                    return;
                }

                if (checkPhone(phone)) {
                    phoneEditText.setError("Số điện thoại không hợp lệ");
                    return;
                }

                dialog = BaseActivity.openLoadingDialog(authenticationActivity);
                callApiRegister(email, password, retypePassword, name, phone);
            }
        });
        return mView;
    }

    private void resetEditText(){
        emailEditText.setText("");
        phoneEditText.setText("");
        retypePasswordEditText.setText("");
        nameEditText.setText("");
        passwordEditText.setText("");
    }

    private void callApiRegister(String email, String password, String retypePassword, String name, String phone) {
        ApiService.apiService.register(email, name, password, retypePassword, phone)
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
                            else if (loginResponse.getToken() != null){
                                sessionManager = new SessionManager(authenticationActivity);
                                sessionManager.saveCustom("verify_token", loginResponse.getToken());
                                resetEditText();
                                authenticationActivity.openFragment(AuthenticationActivity.FRAGMENT_VERIFICATION);
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

    private boolean checkPhone(String phone) {

        if(!phone.startsWith("0")){
            return true;
        }
        if(phone.length() < 10){
            return true;
        }

        return false;
    }
}