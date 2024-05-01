package com.example.clothingstoreapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.fragment.fragmentOfAccountInfoActivity.AccountInfoFragment;
import com.example.clothingstoreapp.fragment.fragmentOfAccountInfoActivity.PasswordChangeFragment;

public class AccountInfoActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView backButtonCart;

    TextView textViewTitle;

    public static final int FRAGMENT_ACCOUNT_INFO = 1;
    public static final int FRAGMENT_PASSWORD_CHANGE = 2;

    public int currentFragment = FRAGMENT_ACCOUNT_INFO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        initView();
        setEvent();
        replaceFragment(new AccountInfoFragment());
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
        textViewTitle.setText("Thông tin tài khoản");
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
                        if (fragmentTag.equals("AccountInfoFragment")) {
                            currentFragment = FRAGMENT_ACCOUNT_INFO;
                            textViewTitle.setText("Thông tin tài khoản");
                        } else if (fragmentTag.equals("PasswordChangeFragment")) {
                            currentFragment = FRAGMENT_PASSWORD_CHANGE;
                            textViewTitle.setText("Thay đổi mật khẩu");
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

    public void openFragment(int fragment){
        if(currentFragment != fragment){
            switch (fragment){
                case FRAGMENT_ACCOUNT_INFO:
                    replaceFragment(new AccountInfoFragment());
                    currentFragment = FRAGMENT_ACCOUNT_INFO;
                    textViewTitle.setText("Thông tin tài khoản");
                    break;
                case FRAGMENT_PASSWORD_CHANGE:
                    replaceFragment(new PasswordChangeFragment());
                    currentFragment = FRAGMENT_PASSWORD_CHANGE;
                    textViewTitle.setText("Thay đổi mật khẩu");
                    break;
            }
        }
    }

    private String getFragmentName(Fragment fragment) {
        // Tùy thuộc vào loại hoặc nội dung của fragment, bạn có thể tạo một FragmentTag tương ứng
         if (fragment instanceof PasswordChangeFragment) {
            return "PasswordChangeFragment";
        }
        return "AccountInfoFragment";
    }

    // chuyển frame
    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        String fragmentTag = getFragmentName(fragment);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

    public void popFragmentChangePassword(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

            getSupportFragmentManager().popBackStack();
            currentFragment = FRAGMENT_ACCOUNT_INFO;
            textViewTitle.setText("Thông tin tài khoản");
        }
    }
}