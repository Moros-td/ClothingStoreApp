package com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;

public class CartEmptyFragment extends Fragment {
    private View mView;
    private Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart_empty, container, false);
        btn = mView.findViewById(R.id.button);
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
