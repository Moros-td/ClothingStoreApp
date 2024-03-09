package com.example.clothingstoreapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.fragment.AccountFragment;
import com.example.clothingstoreapp.fragment.HelpFragment;
import com.example.clothingstoreapp.fragment.HomeFragment;
import com.example.clothingstoreapp.fragment.ProductFragment;
import com.example.clothingstoreapp.fragment.SearchFragment;
import com.google.android.material.badge.BadgeDrawable;;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BaseActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ImageView backButton;
    TextView shopName;

    TextView textCartItemCount;
    int mCartItemCount = 10;

    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_PRODUCT = 2;
    public static final int FRAGMENT_SEARCH = 3;
    public static final int FRAGMENT_HELP = 4;
    public static final int FRAGMENT_ACCOUNT = 5;

    public int currentFragment = FRAGMENT_HOME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();
        setEvent();

        // set mặc định là home
        replaceFragment(new HomeFragment());
    }

    public void initView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
         toolbar = findViewById(R.id.toolbar);

         setSupportActionBar(toolbar);

         if(getSupportActionBar() != null){
             // set hiện nút back
//             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         }

//        backButton = findViewById(R.id.backButton);
        shopName = findViewById(R.id.shopName);

        // set số thông báo account
        BadgeDrawable badgeDrawableAccount = bottomNavigationView.getOrCreateBadge(R.id.action_account);
        badgeDrawableAccount.setVisible(true);
        badgeDrawableAccount.setNumber(1000);

    }


    public void setEvent() {

        // set sự kiện cho nút back
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Xử lý khi người dùng bấm nút back
                // nếu có hơn 1 fragment trong backstack thì pop
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

                    getSupportFragmentManager().popBackStack();
                    // Lấy ra fragment hiện tại trong back stack
                    //-2 là do hết hàm này thì số lượng trong stack mới update
                    FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2);
                    String fragmentTag = backEntry.getName();
                    // So sánh với các fragment đã khai báo
                    //Toast.makeText(BaseActivity.this, String.valueOf(getSupportFragmentManager().getBackStackEntryCount()), Toast.LENGTH_LONG).show();
                    if (fragmentTag != null) {
                        if (fragmentTag.equals("HomeFragment")) {
                            currentFragment = FRAGMENT_HOME;
                            bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        } else if (fragmentTag.equals("ProductFragment")) {
                            currentFragment = FRAGMENT_PRODUCT;
                            bottomNavigationView.getMenu().findItem(R.id.action_product).setChecked(true);
                        } else if (fragmentTag.equals("SearchFragment")) {
                            currentFragment = FRAGMENT_SEARCH;
                            bottomNavigationView.getMenu().findItem(R.id.action_search).setChecked(true);
                        } else if (fragmentTag.equals("HelpFragment")) {
                            currentFragment = FRAGMENT_HELP;
                            bottomNavigationView.getMenu().findItem(R.id.action_help).setChecked(true);
                        } else if (fragmentTag.equals("AccountFragment")) {
                            currentFragment = FRAGMENT_ACCOUNT;
                            bottomNavigationView.getMenu().findItem(R.id.action_account).setChecked(true);
                        }
                        //getSupportFragmentManager().popBackStack();
                    }
                } else {
                    // Thực hiện hành động mặc định khi không có fragment trên back stack
                    finish();
                }
            }
        };

        // Đăng ký callback với dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

        // sự kiện khi click trên menu
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.action_home){
                    openFragment(FRAGMENT_HOME);
                    return true;
                }
                if(id == R.id.action_product){
                    openFragment(FRAGMENT_PRODUCT);
                    return true;
                }
                if(id == R.id.action_search){
                    openFragment(FRAGMENT_SEARCH);
                    return true;
                }
                if(id == R.id.action_help){
                    openFragment(FRAGMENT_HELP);
                    return true;
                }
                if(id == R.id.action_account){
                    openFragment(FRAGMENT_ACCOUNT);
                    return true;
                }

                return false;
            }
        });

        //set sự kiện back
//        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Xử lý sự kiện khi nhấn nút back
//                v.startAnimation(animAlpha);
//
//                callback.handleOnBackPressed();
//
//
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.toolbar_menu, menu);
        final MenuItem shoppinBag = menu.findItem(R.id.cartIcon);

        // hiển thị icon trên menu
        shoppinBag.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        // lấy action view của item đó
        View actionView = shoppinBag.getActionView();

        if(actionView != null){
            // set số sản phẩm trong giỏ
            textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
            setCartBadge(100);

            // sự kiện click vào giỏ hàng
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cartActivity = new Intent(BaseActivity.this, CartBaseActivity.class);

                    startActivity(cartActivity);
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void setCartBadge(int num) {

        if (textCartItemCount != null) {
            if (num < 100) {
                textCartItemCount.setText(String.valueOf(num));
            } else {
                textCartItemCount.setText("99+");
            }
        }
    }


    // mở một frame mới
    public void openFragment(int fragment){
        if(currentFragment != fragment){
            switch (fragment){
                case FRAGMENT_HOME:
                    replaceFragment(new HomeFragment());
                    currentFragment = FRAGMENT_HOME;
                    break;
                case FRAGMENT_PRODUCT:
                    replaceFragment(new ProductFragment());
                    currentFragment = FRAGMENT_PRODUCT;
                    break;
                case FRAGMENT_SEARCH:
                    replaceFragment(new SearchFragment());
                    currentFragment = FRAGMENT_SEARCH;
                    break;
                case FRAGMENT_HELP:
                    replaceFragment(new HelpFragment());
                    currentFragment = FRAGMENT_HELP;
                    break;
                case FRAGMENT_ACCOUNT:
                    replaceFragment(new AccountFragment());
                    currentFragment = FRAGMENT_ACCOUNT;
                    break;
            }
        }
    }

    private String getFragmentName(Fragment fragment) {
        // Tùy thuộc vào loại hoặc nội dung của fragment, bạn có thể tạo một FragmentTag tương ứng
        if (fragment instanceof HomeFragment) {
            return "HomeFragment";
        } else if (fragment instanceof ProductFragment) {
            return "ProductFragment";
        } else if (fragment instanceof SearchFragment) {
            return "SearchFragment";
        } else if (fragment instanceof HelpFragment) {
            return "HelpFragment";
        } else if (fragment instanceof AccountFragment) {
            return "AccountFragment";
        } else {
            // Nếu không thì trả về một tag mặc định hoặc null
            return "DefaultFragment";
        }
    }

    // chuyển frame
    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        String fragmentTag = getFragmentName(fragment);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    }

}