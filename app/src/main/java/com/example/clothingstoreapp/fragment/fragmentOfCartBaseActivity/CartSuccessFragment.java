package com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AddAddressActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.google.android.material.appbar.AppBarLayout;

public class CartSuccessFragment extends Fragment {
    private View mView;
    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart_success, container, false);
        ((CartBaseActivity) getActivity()).setBackButtonVisibility(false);
        btn = mView.findViewById(R.id.button_shopping);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BaseActivity.class);
                startActivity(intent);
            }
        });
        return mView;
    }
}
