package com.example.clothingstoreapp.fragment.fragmentOfAccountInfoActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.clothingstoreapp.R;

public class PasswordChangeFragment extends Fragment {

    Button submitBtn;
    public PasswordChangeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_password_change, container, false);
        submitBtn = mView.findViewById(R.id.submitBtn);
        final Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
            }
        });
        return mView;
    }
}