package com.example.clothingstoreapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.fragment.fragmenOfBaseActivity.ListProductFragment;
import com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity.CartExistFragment;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.CheckItemResponse;
import com.example.clothingstoreapp.response.ErrResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductActivity extends AppCompatActivity {
    Toolbar toolbarListProduct;
    ImageView backButton;
    TextView textCartItemCount;
    SessionManager sessionManager;
    ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int categoryId = bundle.getInt("category_id");
            ListProductFragment fragment = new ListProductFragment();

            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putInt("category_id", categoryId);

            fragment.setArguments(fragmentArgs);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerListProduct, fragment)
                    .commit();
        }

        initView();
        setEvent();
    }

    public void setEvent() {
        // set sự kiện cho nút back
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Xử lý khi người dùng bấm nút back
                // nếu có fragment trong backstack thì pop
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    getSupportFragmentManager().popBackStack();
                } else {
                    // Thực hiện hành động mặc định khi không có fragment trên back stack
                    returnResultCloseListProduct();
                }
            }
        };

        // Đăng ký callback với dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

        // sự kiện trên tool bar
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nhấn nút back
                v.startAnimation(animAlpha);

                callback.handleOnBackPressed();

            }
        });
        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == ResultCodeActivity.LOGIN_SUCCESS) {
                        //login thành công
                        Intent cartActivity = new Intent(ListProductActivity.this, CartBaseActivity.class);

                        //startActivity(cartActivity);
                        activityLauncher.launch(cartActivity);
                    }
                    else if(result.getResultCode() == ResultCodeActivity.CLOSE_CART){
                        if(sessionManager.isLoggedIn()){
                            callApiSetCartCountItem(sessionManager.getJwt(), sessionManager.getCustom("email"));
                        }
                    }
                });
    }

    public void returnResultCloseListProduct() {
        setResult(ResultCodeActivity.CLOSE_CART);
        finish();
    }

    public void initView() {
        toolbarListProduct = findViewById(R.id.toolbar_list_product);
        setSupportActionBar(toolbarListProduct);
        if (getSupportActionBar() != null) {
            // set hiện nút back
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        backButton = findViewById(R.id.backButton);
        sessionManager = new SessionManager(ListProductActivity.this);

    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerListProduct, fragment);
        fragmentTransaction.commit();
    }
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
    public void setCartBadge(int num) {
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

    public int getCartBadge() {
        if (textCartItemCount != null) {
            int num = Integer.parseInt(textCartItemCount.getText().toString());
            return num;
        }
        return 0;
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
                callApiSetCartCountItem(sessionManager.getJwt(), sessionManager.getCustom("email"));
            }
            else{
                setCartBadge(0);
            }

            // sự kiện click vào giỏ hàng
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cartActivity = new Intent(ListProductActivity.this, CartBaseActivity.class);
                    activityLauncher.launch(cartActivity);
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
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
    public void callApiSetCartCountItem(String token, String email) {
        Dialog dialog;
        dialog = openLoadingDialog(ListProductActivity.this);
        ApiService.apiService.checkItem(token, email).enqueue(new Callback<CheckItemResponse>() {
            @Override
            public void onResponse(Call<CheckItemResponse> call, Response<CheckItemResponse> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (response.isSuccessful()) {
                    CheckItemResponse temp = response.body();
                    int count = temp.getNumberOfItem();
                    setCartBadge(count);
                } else {
                    Gson gson = new Gson();

                    if(response.errorBody() != null) {
                        try {
                            ErrResponse errResponse = gson.fromJson(response.errorBody().string(), ErrResponse.class);
                            BaseActivity.openErrorDialog(ListProductActivity.this, errResponse.getErr());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        BaseActivity.openErrorDialog(ListProductActivity.this, "Lỗi");
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckItemResponse> call, Throwable throwable) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                BaseActivity.openErrorDialog(ListProductActivity.this, "Không thể truy cập API, vui lòng thử lại sau!");
            }

        });
    }
}