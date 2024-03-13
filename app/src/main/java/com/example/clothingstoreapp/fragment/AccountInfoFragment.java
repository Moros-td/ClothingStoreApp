package com.example.clothingstoreapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AccountInfoActivity;

public class AccountInfoFragment extends Fragment {
    Button updateInfoBtn, changePasswordBtn;
    AccountInfoActivity accountInfoActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =  inflater.inflate(R.layout.fragment_account_info, container, false);
        accountInfoActivity = (AccountInfoActivity) getContext();
        updateInfoBtn = mView.findViewById(R.id.updateInfoBtn);
        changePasswordBtn = mView.findViewById(R.id.changePasswordBtn);
        final Animation animAlpha = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                accountInfoActivity.openFragment(AccountInfoActivity.FRAGMENT_PASSWORD_CHANGE);
            }
        });
        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
            }
        });
        return mView;
    }
}