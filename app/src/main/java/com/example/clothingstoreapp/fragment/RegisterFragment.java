package com.example.clothingstoreapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AuthenticationActivity;

public class RegisterFragment extends Fragment {

    Button registerBtn;
    AuthenticationActivity authenticationActivity;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_register, container, false);
        authenticationActivity = (AuthenticationActivity) getContext();
        registerBtn = mView.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationActivity.openFragment(AuthenticationActivity.FRAGMENT_VERIFICATION);
            }
        });
        return mView;
    }
}