package com.example.clothingstoreapp.fragment.fragmentOfAuthenticationActivity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AuthenticationActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.entity.CustomerEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.ErrResponse;
import com.example.clothingstoreapp.response.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    TextView registerTextView, errTextView, forgotPasswordTextView;

    EditText emailEditText, passwordEditText;
    Button loginBtn;

    Dialog dialog;

    SessionManager sessionManager;

    AuthenticationActivity authenticationActivity;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_login, container, false);
        registerTextView = mview.findViewById(R.id.registerTextView);
        errTextView = mview.findViewById(R.id.errorTextView);
        forgotPasswordTextView = mview.findViewById(R.id.forgotPasswordTextView);
        loginBtn = mview.findViewById(R.id.loginBtn);
        emailEditText = mview.findViewById(R.id.emailEditText);
        passwordEditText = mview.findViewById(R.id.passwordEditText);
        authenticationActivity = (AuthenticationActivity) getContext();
        // Ẩn TextView
        //errTextView.setVisibility(View.GONE);

        final Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                authenticationActivity.openFragment(AuthenticationActivity.FRAGMENT_REGISTER);
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                authenticationActivity.openFragment(AuthenticationActivity.FRAGMENT_FORGOT_PASSWORD);
                //Toast.makeText(getContext(), String.valueOf(authenticationActivity.currentFragment), Toast.LENGTH_LONG).show();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errTextView.setVisibility(View.GONE);
                dialog = BaseActivity.openLoadingDialog(authenticationActivity);
                callApiLogin();
            }
        });

        return mview;
    }

    private void resetEditText(){
        emailEditText.setText("");
        passwordEditText.setText("");
    }
    private void callApiLogin() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setEmail(emailEditText.getText().toString().trim());
        customerEntity.setPassword(passwordEditText.getText().toString().trim());
        resetEditText();
        if(isEmailValid(customerEntity.getEmail())){
            ApiService.apiService.logIn(customerEntity.getEmail(),
                            customerEntity.getPassword())
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
                                    sessionManager.saveJwt(loginResponse.getToken());
                                    sessionManager.saveCustom("email", customerEntity.getEmail());
                                    //Toast.makeText(getContext(), loginResponse.getToken(), Toast.LENGTH_LONG).show();

                                    // finish và trả về mã ok
                                    authenticationActivity.returnResultOk();
                                }
                            }
                            else {
                                //Toast.makeText(getContext(), "Api không trả về data", Toast.LENGTH_LONG).show();
                                ResponseBody responseBody = response.errorBody();
                                Gson gson = new Gson();
                                try {
                                    if(responseBody != null){
                                        ErrResponse errResponse = gson.fromJson(responseBody.string(), ErrResponse.class);
                                        errTextView.setText(errResponse.getErr());
                                        errTextView.setVisibility(View.VISIBLE);
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
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
        else{
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            errTextView.setText("Vui lòng nhập đúng định dạng email");
            errTextView.setVisibility(View.VISIBLE);
        }


    }
    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}