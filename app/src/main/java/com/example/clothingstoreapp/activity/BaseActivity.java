package com.example.clothingstoreapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.fragment.fragmenOfBaseActivity.AccountFragment;
import com.example.clothingstoreapp.fragment.fragmenOfBaseActivity.HelpFragment;
import com.example.clothingstoreapp.fragment.fragmenOfBaseActivity.HomeFragment;
import com.example.clothingstoreapp.fragment.fragmenOfBaseActivity.ProductFragment;
import com.example.clothingstoreapp.fragment.fragmenOfBaseActivity.SearchFragment;
import com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity.CartEmptyFragment;
import com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity.CartExistFragment;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.CheckItemResponse;
import com.example.clothingstoreapp.response.ErrResponse;
import com.google.android.material.badge.BadgeDrawable;;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ImageView backButton;
    TextView shopName;

    TextView textCartItemCount;
    int mCartItemCount = 10;

    SessionManager sessionManager;

    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_PRODUCT = 2;
    public static final int FRAGMENT_SEARCH = 3;
    public static final int FRAGMENT_HELP = 4;
    public static final int FRAGMENT_ACCOUNT = 5;

    public int currentFragment = FRAGMENT_HOME;

    ActivityResultLauncher<Intent> activityLauncher;

//    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();
        setEvent();

        // set mặc định là home
        replaceFragment(new HomeFragment());
    }

    public void enableBack(){
        if(getSupportActionBar() != null){
            // set hiện nút back
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

        sessionManager = new SessionManager(BaseActivity.this);
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

         activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //login thành công
                        Intent cartActivity = new Intent(BaseActivity.this, CartBaseActivity.class);

                        startActivity(cartActivity);
                    }
                });
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
            if(sessionManager.isLoggedIn()){
//                dialog = openLoadingDialog(BaseActivity.this);
                callApiSetCartCountItem(sessionManager.getJwt(), sessionManager.getCustom("email"));
            }
            else{
                setCartBadge(0);
            }

            // sự kiện click vào giỏ hàng
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sessionManager.isLoggedIn()){
                        Intent cartActivity = new Intent(BaseActivity.this, CartBaseActivity.class);

                        startActivity(cartActivity);
                    }
                    // chưa login
                    else{
                        Intent intent = new Intent(BaseActivity.this, AuthenticationActivity.class);
                        activityLauncher.launch(intent);
                    }
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    public void callApiSetCartCountItem(String token, String email) {
    Dialog dialog;
        dialog = openLoadingDialog(BaseActivity.this);
        ApiService.apiService.checkItem(token, email).enqueue(new Callback<CheckItemResponse>() {
            @Override
            public void onResponse(Call<CheckItemResponse> call, Response<CheckItemResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    CheckItemResponse temp = response.body();
                    int count = temp.getNumberOfItem();
                    setCartBadge(count);
                } else {
                    Gson gson = new Gson();

                    if(response.errorBody() != null) {
                        try {
                            ErrResponse errResponse = gson.fromJson(response.errorBody().string(), ErrResponse.class);
                            BaseActivity.openErrorDialog(BaseActivity.this, errResponse.getErr());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        BaseActivity.openErrorDialog(BaseActivity.this, "Lỗi");
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckItemResponse> call, Throwable throwable) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                BaseActivity.openErrorDialog(BaseActivity.this, "Không thể truy cập API, vui lòng thử lại sau!");
            }

        });
    }

    private void setCartBadge(int num) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textCartItemCount.getLayoutParams();
        if (textCartItemCount != null) {
            if (num < 100) {
                params.width = dpToPx(20);
                textCartItemCount.setText(String.valueOf(num));
                params.setMarginEnd(-20);
            } else {
                params.width = dpToPx(30);
                params.setMarginEnd(-40);
                textCartItemCount.setText("99+");
            }
        }
        textCartItemCount.setLayoutParams(params);
    }
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
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

    public void initReplaceCurrentFragment(int fragmentNum) {

        if(fragmentNum == FRAGMENT_ACCOUNT && fragmentNum == currentFragment){
            replaceCurrentFragment(new AccountFragment());
        }
        else if(fragmentNum == FRAGMENT_PRODUCT && fragmentNum == currentFragment){
            replaceCurrentFragment(new ProductFragment());
        }
        else if(fragmentNum == FRAGMENT_SEARCH && fragmentNum == currentFragment){
            replaceCurrentFragment(new SearchFragment());
        }
    }

    private void replaceCurrentFragment(Fragment fragment){
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

            getSupportFragmentManager().popBackStack();
            // Lấy ra fragment hiện tại trong back stack
            //-2 là do hết hàm này thì số lượng trong stack mới update
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            String fragmentTag = getFragmentName(fragment);
            fragmentTransaction.addToBackStack(fragmentTag);
            fragmentTransaction.commit();
        }
    }


    public static Dialog openLoadingDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);

        Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(false);

        dialog.show();
        return dialog;
    }

    public static void openSuccessDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.success_dialog);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // setView trên dialog
        TextView successTextView = dialog.findViewById(R.id.successTextView);

        successTextView.setText(message);

        // Khởi tạo Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Đóng dialog sau 3 giây
                dialog.dismiss();
            }
        }, 2000); // Thời gian đợi 2 giây trước khi đóng dialog

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(false);

        dialog.show();
    }

    public static void openErrorDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_dialog);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(true);

        // setView trên dialog
        TextView errorTextView = dialog.findViewById(R.id.errorTextView);
        Button oKConfirmBtn = dialog.findViewById(R.id.oKConfirmBtn);

        errorTextView.setText(message);

        //set event
        oKConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}