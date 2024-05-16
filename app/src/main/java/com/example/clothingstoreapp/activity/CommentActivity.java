package com.example.clothingstoreapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.adapter.CommentAdapter;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.entity.CommentEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView backButtonCart;

    TextView textViewTitle;

    private List<CommentEntity> list;
    private CommentAdapter commentAdapter;
    private RecyclerView commentRCV;
    private SessionManager sessionManager;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        setEvent();

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        // Kiểm tra xem dữ liệu đã được truyền qua không
        String productCode = bundle.getString("productCode", "");
        callApiGetAllCommentForProduct(productCode);
    }

    public void initView() {
        toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        commentRCV = findViewById(R.id.commentRCV);
        if (getSupportActionBar() != null) {
            // set hiện nút back
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        backButtonCart = findViewById(R.id.backButtonCart);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText("Đánh giá");
        sessionManager = new SessionManager(CommentActivity.this);

    }

    public void setEvent() {
        // set sự kiện cho nút back
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

    private void callApiGetAllCommentForProduct(String productCode) {
        dialog = BaseActivity.openLoadingDialog(CommentActivity.this);
        ApiService.apiService.getAllCommentForProduct(productCode)
                .enqueue(new Callback<List<CommentEntity>>() {
                    @Override
                    public void onResponse(Call<List<CommentEntity>> call, Response<List<CommentEntity>> response) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        list = response.body();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentActivity.this);
                        commentRCV.setLayoutManager(linearLayoutManager);
                        commentAdapter = new CommentAdapter(list);
                        commentRCV.setAdapter(commentAdapter);

                    }

                    @Override
                    public void onFailure(Call<List<CommentEntity>> call, Throwable throwable) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        BaseActivity.openErrorDialog(CommentActivity.this, "Không thể truy cập api");
                    }
                });
    }
}