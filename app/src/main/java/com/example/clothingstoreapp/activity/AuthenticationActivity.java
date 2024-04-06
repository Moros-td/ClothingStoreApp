package com.example.clothingstoreapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.fragment.fragmentOfAuthenticationActivity.ForgotPasswordFragment;
import com.example.clothingstoreapp.fragment.fragmentOfAuthenticationActivity.LoginFragment;
import com.example.clothingstoreapp.fragment.fragmentOfAuthenticationActivity.RegisterFragment;
import com.example.clothingstoreapp.fragment.fragmentOfAuthenticationActivity.VerificationFragment;

public class AuthenticationActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView backButtonCart;

    TextView textViewTitle;

    public static final int FRAGMENT_LOGIN = 1;
    public static final int FRAGMENT_REGISTER = 2;
    public static final int FRAGMENT_FORGOT_PASSWORD = 3;
    public static final int FRAGMENT_VERIFICATION = 4;

    public int currentFragment = FRAGMENT_LOGIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        initView();
        setEvent();
        replaceFragment(new LoginFragment());
    }

    public void initView() {
        toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // set hiện nút back
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        backButtonCart = findViewById(R.id.backButtonCart);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText("Đăng nhập");
    }

    public void setEvent() {
        // set sự kiện cho nút back
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Xử lý khi người dùng bấm nút back
                // nếu có fragment trong backstack thì pop
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

                    getSupportFragmentManager().popBackStack();
                    // Lấy ra fragment hiện tại trong back stack
                    //-2 là do hết hàm này thì số lượng trong stack mới update
                    FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2);
                    String fragmentTag = backEntry.getName();
                    // So sánh với các fragment đã khai báo
                    //Toast.makeText(BaseActivity.this, String.valueOf(getSupportFragmentManager().getBackStackEntryCount()), Toast.LENGTH_LONG).show();
                    if (fragmentTag != null) {
                        if (fragmentTag.equals("LoginFragment")) {
                            currentFragment = FRAGMENT_LOGIN;
                            textViewTitle.setText("Đăng nhập");
                        } else if (fragmentTag.equals("RegisterFragment")) {
                            currentFragment = FRAGMENT_REGISTER;
                            textViewTitle.setText("Đăng kí");
                        } else if (fragmentTag.equals("ForgotPasswordFragment")) {
                            currentFragment = FRAGMENT_FORGOT_PASSWORD;
                            textViewTitle.setText("Quên mật khẩu");
                        }
                        else if (fragmentTag.equals("VerificationFragment")) {
                            currentFragment = FRAGMENT_VERIFICATION;
                            textViewTitle.setText("Xác thực");
                        }
                    }
                } else {
                    // Thực hiện hành động mặc định khi không có fragment trên back stack
                    finish();
                }
            }
        };

        // Đăng ký callback với dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

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

    public void popVerifyFragment(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            currentFragment = FRAGMENT_REGISTER;
            textViewTitle.setText("Đăng kí");
        }
    }

    public void openFragment(int fragment){
        if(currentFragment != fragment){
            switch (fragment){
                case FRAGMENT_LOGIN:
                    replaceFragment(new LoginFragment());
                    currentFragment = FRAGMENT_LOGIN;
                    textViewTitle.setText("Đăng nhập");
                    break;
                case FRAGMENT_REGISTER:
                    replaceFragment(new RegisterFragment());
                    currentFragment = FRAGMENT_REGISTER;
                    textViewTitle.setText("Đăng kí");
                    break;
                case FRAGMENT_FORGOT_PASSWORD:
                    replaceFragment(new ForgotPasswordFragment());
                    currentFragment = FRAGMENT_FORGOT_PASSWORD;
                    textViewTitle.setText("Quên mật khẩu");
                    break;
                case FRAGMENT_VERIFICATION:
                    replaceFragment(new VerificationFragment());
                    currentFragment = FRAGMENT_VERIFICATION;
                    textViewTitle.setText("Xác thưc");
                    break;
            }
        }
    }

    private String getFragmentName(Fragment fragment) {
        // Tùy thuộc vào loại hoặc nội dung của fragment, bạn có thể tạo một FragmentTag tương ứng
        if (fragment instanceof ForgotPasswordFragment) {
            return "ForgotPasswordFragment";
        } else if (fragment instanceof RegisterFragment) {
            return "RegisterFragment";
        } else if (fragment instanceof VerificationFragment) {
            return "VerificationFragment";
        }
        return "LoginFragment";
    }

    // chuyển frame
    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        String fragmentTag = getFragmentName(fragment);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

    public void returnResultOk(){
        setResult(Activity.RESULT_OK);
        finish();
    }
}