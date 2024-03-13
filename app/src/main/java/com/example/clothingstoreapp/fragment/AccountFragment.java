package com.example.clothingstoreapp.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.example.clothingstoreapp.activity.AuthenticationActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.google.android.material.navigation.NavigationView;

public class AccountFragment extends Fragment {

    private NavigationView navigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_account, container, false);
        navigationView = mview.findViewById(R.id.account_view);

        String token = "b";
        if(token.isEmpty()){
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
            return mview;
        }
        else{
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_account_after_login);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();

                    if(id == R.id.manageOrderItem){

                    }
                    if(id == R.id.manageAccountItem){
                        Intent intent = new Intent(getContext(), AccountInfoActivity.class);

                        startActivity(intent);
                    }
                    if(id == R.id.locationItem){

                    }
                    if(id == R.id.logoutItem){
                        Toast.makeText(getContext(), "hehe", Toast.LENGTH_LONG).show();
                    }

                    return false;
                }
            });
        }
        return mview;
    }
}