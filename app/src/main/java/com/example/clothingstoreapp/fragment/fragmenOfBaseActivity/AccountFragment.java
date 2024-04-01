package com.example.clothingstoreapp.fragment.fragmenOfBaseActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AccountInfoActivity;
import com.example.clothingstoreapp.activity.AddressManagementActivity;
import com.example.clothingstoreapp.activity.AuthenticationActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.example.clothingstoreapp.activity.OrderManagementActivity;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.ErrResponse;
import com.example.clothingstoreapp.response.LoginResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private NavigationView navigationView;
    private SessionManager sessionManager;

    private Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_account, container, false);
        navigationView = mview.findViewById(R.id.account_view);

        sessionManager = new SessionManager(getContext());
        String token = "";

        if(sessionManager.isLoggedIn()){
            token = sessionManager.getJwt();
        }
        if(token.isEmpty()){
            showMenuBeforeLogin();
            return mview;
        }
        else{
            showMenuAfterLogin();
        }
        return mview;
    }

    public void showMenuBeforeLogin(){
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_account);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.loginItem){
                    Intent authenticationActivity = new Intent(getContext(), AuthenticationActivity.class);

                    startActivity(authenticationActivity);
                }

                return false;
            }
        });
    }

    public void showMenuAfterLogin(){
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_account_after_login);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.manageOrderItem){
                    Intent intent = new Intent(getContext(), OrderManagementActivity.class);

                    startActivity(intent);
                }
                if(id == R.id.manageAccountItem){
                    Intent intent = new Intent(getContext(), AccountInfoActivity.class);

                    startActivity(intent);
                }
                if(id == R.id.locationItem){
                    Intent intent = new Intent(getContext(), AddressManagementActivity.class);

                    startActivity(intent);
                }
                if(id == R.id.logoutItem){
                    dialog = BaseActivity.openLoadingDialog(getContext());
                    callApiLogout();
                }

                return false;
            }
        });
    }

    private void callApiLogout() {
        sessionManager = new SessionManager(getContext());
        if(sessionManager.isLoggedIn()){
            ApiService.apiService.loginOut(sessionManager.getJwt(), sessionManager.getCustom("email"))
                    .enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    LoginResponse loginResponse = response.body();
                    if(loginResponse != null){
                        if(loginResponse.getErr() != null){
                            if(loginResponse.getErr().equals("Unauthenticated")){
                                sessionManager = new SessionManager(getContext());
                                sessionManager.logout();
                                sessionManager.deleteCustom("email");
                                showMenuBeforeLogin();

                                BaseActivity baseActivity = (BaseActivity) getContext();
                                baseActivity.initReplaceCurrentFragment(BaseActivity.FRAGMENT_ACCOUNT);
                            }
                            else{
                                Toast.makeText(getContext(), loginResponse.getErr(), Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            sessionManager = new SessionManager(getContext());
                            sessionManager.logout();
                            sessionManager.deleteCustom("email");
                            showMenuBeforeLogin();

                            BaseActivity baseActivity = (BaseActivity) getContext();
                            baseActivity.initReplaceCurrentFragment(BaseActivity.FRAGMENT_ACCOUNT);
                        }
                    }
                    else {
                        //Toast.makeText(getContext(), "Api không trả về data", Toast.LENGTH_LONG).show();
                        ResponseBody responseBody = response.errorBody();
                        Gson gson = new Gson();
                        try {
                            if(responseBody != null){
                                ErrResponse errResponse = gson.fromJson(responseBody.string(), ErrResponse.class);
                                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(), "Không thể truy cập api", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        sessionManager = new SessionManager(getContext());
        String token = "";
        if(sessionManager.isLoggedIn()){
            token = sessionManager.getJwt();
        }
        if(token.isEmpty()){
            showMenuBeforeLogin();
        }
        else{
            showMenuAfterLogin();
        }
    }

}