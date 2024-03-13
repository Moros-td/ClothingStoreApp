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
import com.example.clothingstoreapp.fragment.AddressInfoFragment;
import com.example.clothingstoreapp.fragment.AddressManagementFragment;
import com.example.clothingstoreapp.fragment.OrderDetailFragment;
import com.example.clothingstoreapp.fragment.OrderManagementFragment;

public class AddressManagementActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView backButtonCart;

    TextView textViewTitle;

    public static final int FRAGMENT_ADDRESS_MANAGEMENT = 1;
    public static final int FRAGMENT_ADDRESS_INFO = 2;

    public int currentFragment = FRAGMENT_ADDRESS_MANAGEMENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);

        initView();
        setEvent();
        replaceFragment(new AddressManagementFragment(), null);
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
        textViewTitle.setText("Quản lý địa chỉ");
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
                        if (fragmentTag.equals("AddressManagementFragment")) {
                            currentFragment = FRAGMENT_ADDRESS_MANAGEMENT;
                            textViewTitle.setText("Quản lý địa chỉ");
                        } else if (fragmentTag.equals("AddressInfoFragment")) {
                            currentFragment = FRAGMENT_ADDRESS_INFO;
                            textViewTitle.setText("Chi tiết địa chỉ");
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
                case FRAGMENT_ADDRESS_MANAGEMENT:
                    if(bundle != null)
                        replaceFragment(new AddressInfoFragment(), bundle);
                    else
                        replaceFragment(new AddressInfoFragment(), null);
                    currentFragment = FRAGMENT_ADDRESS_MANAGEMENT;
                    textViewTitle.setText("Quản lý địa chỉ");
                    break;
                case FRAGMENT_ADDRESS_INFO:
                    if(bundle != null)
                        replaceFragment(new AddressInfoFragment(), bundle);
                    else
                        replaceFragment(new AddressInfoFragment(), null);
                    currentFragment = FRAGMENT_ADDRESS_INFO;
                    textViewTitle.setText("Thông tin địa chỉ");
                    break;
            }
        }
    }

    private String getFragmentName(Fragment fragment) {
        // Tùy thuộc vào loại hoặc nội dung của fragment, bạn có thể tạo một FragmentTag tương ứng
        if (fragment instanceof AddressInfoFragment) {
            return "AddressInfoFragment";
        }
        return "AddressManagementFragment";
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