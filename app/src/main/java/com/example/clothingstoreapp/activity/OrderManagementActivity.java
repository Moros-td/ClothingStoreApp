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
import com.example.clothingstoreapp.fragment.AccountInfoFragment;
import com.example.clothingstoreapp.fragment.OrderDetailFragment;
import com.example.clothingstoreapp.fragment.OrderManagementFragment;
import com.example.clothingstoreapp.fragment.PasswordChangeFragment;

public class OrderManagementActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView backButtonCart;

    TextView textViewTitle;

    public static final int FRAGMENT_ORDER_MANAGEMENT = 1;
    public static final int FRAGMENT_ORDER_DETAIL = 2;

    public int currentFragment = FRAGMENT_ORDER_MANAGEMENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        initView();
        setEvent();
        replaceFragment(new OrderManagementFragment(), null);
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
        textViewTitle.setText("Quản lý đơn hàng");
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
                        if (fragmentTag.equals("OrderManagementFragment")) {
                            currentFragment = FRAGMENT_ORDER_MANAGEMENT;
                            textViewTitle.setText("Quản lý đơn hàng");
                        } else if (fragmentTag.equals("OrderDetailFragment")) {
                            currentFragment = FRAGMENT_ORDER_DETAIL;
                            textViewTitle.setText("Chi tiết đơn hàng");
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

    public void openFragment(int fragment, Bundle bundle){
        if(currentFragment != fragment){
            switch (fragment){
                case FRAGMENT_ORDER_MANAGEMENT:
                    if(bundle != null)
                        replaceFragment(new OrderManagementFragment(), bundle);
                    else
                        replaceFragment(new OrderManagementFragment(), null);
                    currentFragment = FRAGMENT_ORDER_MANAGEMENT;
                    textViewTitle.setText("Quản lý đơn hàng");
                    break;
                case FRAGMENT_ORDER_DETAIL:
                    if(bundle != null)
                        replaceFragment(new OrderDetailFragment(), bundle);
                    else
                        replaceFragment(new OrderDetailFragment(), null);
                    currentFragment = FRAGMENT_ORDER_DETAIL;
                    textViewTitle.setText("Chi tiết đơn hàng");
                    break;
            }
        }
    }

    private String getFragmentName(Fragment fragment) {
        // Tùy thuộc vào loại hoặc nội dung của fragment, bạn có thể tạo một FragmentTag tương ứng
        if (fragment instanceof OrderDetailFragment) {
            return "OrderDetailFragment";
        }
        return "OrderManagementFragment";
    }

    // chuyển frame
    public void replaceFragment(Fragment fragment, Bundle data) {
        if (data != null) {
            fragment.setArguments(data);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        String fragmentTag = getFragmentName(fragment);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }
}