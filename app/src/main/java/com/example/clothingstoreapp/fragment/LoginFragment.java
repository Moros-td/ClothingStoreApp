package com.example.clothingstoreapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class LoginFragment extends Fragment {

    TextView registerTextView, errTextView, forgotPasswordTextView;
    Button loginBtn;

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
        return mview;
    }
}