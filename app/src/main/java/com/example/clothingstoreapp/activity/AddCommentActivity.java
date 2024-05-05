package com.example.clothingstoreapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.entity.OrderItemEntity;
import com.example.clothingstoreapp.entity.ProductEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.LoginResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCommentActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView backButtonCart;

    TextView textViewTitle, ratingText;
    private SessionManager sessionManager;
    RatingBar ratingBar;
    TextInputEditText commentEditText;
    Button btnSend;
    Dialog dialog;
    OrderItemEntity orderItemEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        initView();
        initEvent();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderItemEntity = (OrderItemEntity) bundle.getSerializable("orderItem");
        }
    }

    private void initEvent() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String comment = commentEditText.getText().toString();
                if(comment.isEmpty()){
                    commentEditText.setError("Không được bỏ trống");
                }
                callApiAddComment(rating, comment);
            }
        });
        ratingBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ffc922")));
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating <= 1){
                    ratingText.setText("Rất tệ");
                }
                else if(rating <= 2){
                    ratingText.setText("Tệ");
                }
                else if(rating <= 3){
                    ratingText.setText("Trung bình");
                }
                else if(rating <= 4){
                    ratingText.setText("Tốt");
                }
                else if(rating <= 5){
                    ratingText.setText("Rất tốt");
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                    finish();
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

    private void callApiAddComment(float rating, String comment) {
        if(sessionManager.isLoggedIn()){
            String token = sessionManager.getJwt();
            String email = sessionManager.getCustom("email");
            dialog = BaseActivity.openLoadingDialog(AddCommentActivity.this);
            ApiService.apiService.addComment(token, email, orderItemEntity.getOrderItemId(), orderItemEntity.getProduct().getProductCode(), rating, comment)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            LoginResponse response1 = response.body();
                            if(response1 != null){
                                if("done".equals(response1.getMessage())){
                                    BaseActivity.openSuccessDialog(AddCommentActivity.this, "Thêm đánh giá thánh công!");

                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // set result để bên fragment reload
                                            setResult(Activity.RESULT_OK);
                                            finish();
                                        }
                                    }, 2000); // 3000 milliseconds = 3 second
                                }
                                else{
                                    BaseActivity.openErrorDialog(AddCommentActivity.this, response1.getErr());
                                }
                            }else{
                                BaseActivity.openErrorDialog(AddCommentActivity.this, "Lỗi");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            BaseActivity.openErrorDialog(AddCommentActivity.this, "Không thể truy cập API, vui lòng thử lại sau!");
                        }
                    });
        }
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
        textViewTitle.setText("Thêm đánh giá");
        sessionManager = new SessionManager(AddCommentActivity.this);

        commentEditText = findViewById(R.id.commentEditText);
        ratingBar = findViewById(R.id.ratingBar);
        btnSend = findViewById(R.id.btnSend);
        ratingText = findViewById(R.id.ratingText);
    }
}