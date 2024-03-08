package com.example.clothingstoreapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.entity.ProductEntity;
import com.example.clothingstoreapp.fragment.CartEmptyFragment;
import com.example.clothingstoreapp.fragment.CartExistFragment;

import java.util.ArrayList;
import java.util.List;

public class CartBaseActivity extends AppCompatActivity {

    Toolbar toolbarCart;
    ImageView backButtonCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initView();
        setEvent();

        //test cart empty or exist
        Fragment fragment;
//        fragment = new CartEmptyFragment();
//        replaceFragment(fragment);
        fragment = new CartExistFragment();
        replaceFragment(fragment);
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
                    finish();
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
}