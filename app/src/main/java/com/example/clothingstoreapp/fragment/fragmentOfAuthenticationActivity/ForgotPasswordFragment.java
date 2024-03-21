package com.example.clothingstoreapp.fragment.fragmentOfAuthenticationActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AuthenticationActivity;

public class ForgotPasswordFragment extends Fragment {

    Button submitBtn;
    AuthenticationActivity authenticationActivity;
    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        authenticationActivity = (AuthenticationActivity) getContext();
        submitBtn = mView.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationActivity.openFragment(AuthenticationActivity.FRAGMENT_VERIFICATION);
            }
        });
        return mView;
    }
}