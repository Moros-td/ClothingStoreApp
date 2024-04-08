package com.example.clothingstoreapp.activity;

import static java.security.AccessController.getContext;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity.CartEmptyFragment;
import com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity.CartExistFragment;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.CheckItemResponse;
import com.example.clothingstoreapp.response.ErrResponse;
import com.example.clothingstoreapp.response.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartBaseActivity extends AppCompatActivity {

    Toolbar toolbarCart;
    ImageView backButtonCart;
    SessionManager sessionManager;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dialog = BaseActivity.openLoadingDialog(CartBaseActivity.this);

        initView();
        setEvent();

        sessionManager = new SessionManager(CartBaseActivity.this);
        //test cart empty or exist
        ApiService.apiService.checkItem(sessionManager.getJwt(), sessionManager.getCustom("email")).enqueue(new Callback<CheckItemResponse>() {
            @Override
            public void onResponse(Call<CheckItemResponse> call, Response<CheckItemResponse> response) {

                if (response.isSuccessful()) {
                    CheckItemResponse temp = response.body();
                    int count = temp.getNumberOfItem();
                    if(count != 0){
                        Fragment fragment = new CartExistFragment();
                        replaceFragment(fragment);
                    }
                    else{
                        Fragment fragment = new CartEmptyFragment();
                        replaceFragment(fragment);
                    }
                } else {
                    Gson gson = new Gson();

                    if(response.errorBody() != null) {
                        try {
                            ErrResponse errResponse = gson.fromJson(response.errorBody().string(), ErrResponse.class);
                            BaseActivity.openErrorDialog(CartBaseActivity.this, errResponse.getErr());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        BaseActivity.openErrorDialog(CartBaseActivity.this, "Lỗi");
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<CheckItemResponse> call, Throwable throwable) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                BaseActivity.openErrorDialog(CartBaseActivity.this, "Không thể truy cập API, vui lòng thử lại sau!");
            }

        });


    }


    public void initView() {
        toolbarCart = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbarCart);
        if (getSupportActionBar() != null) {
            // set hiện nút back
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        backButtonCart = findViewById(R.id.backButtonCart);

    }

    public void setEvent() {
        // set sự kiện cho nút back
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Xử lý khi người dùng bấm nút back
                // nếu có fragment trong backstack thì pop
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    getSupportFragmentManager().popBackStack();
                } else {
                    // Thực hiện hành động mặc định khi không có fragment trên back stack
                    returnResultCloseCart();
                }
            }
        };

        // Đăng ký callback với dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

        // sự kiện trên tool bar
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        backButtonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nhấn nút back
                v.startAnimation(animAlpha);

                callback.handleOnBackPressed();

            }
        });
    }


    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerCart, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    public void returnResultCloseCart(){
        setResult(ResultCodeActivity.CLOSE_CART);
        finish();
    }
    public void setBackButtonVisibility(boolean isVisible) {
        ImageView backButtonCart = findViewById(R.id.backButtonCart);
        backButtonCart.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}