package com.example.clothingstoreapp.fragment.fragmenOfBaseActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CommentActivity;
import com.example.clothingstoreapp.activity.ListProductActivity;
import com.example.clothingstoreapp.activity.ProductDetailActivity;
import com.example.clothingstoreapp.adapter.AddressManagementAdapter;
import com.example.clothingstoreapp.adapter.CommentAdapter;
import com.example.clothingstoreapp.adapter.PhotoAdapter;
import com.example.clothingstoreapp.adapter.PhotoProductAdapter;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.entity.CommentEntity;
import com.example.clothingstoreapp.entity.Photo;
import com.example.clothingstoreapp.entity.ProductEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.BooleanResponse;
import com.example.clothingstoreapp.response.CartCodeResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailFragment extends Fragment {

    private ViewPager viewPager;
    private CircleIndicator indicator;
    private PhotoProductAdapter photoProductAdapter;
    private View mView, productColorView;
    private TextView quantityTextView, productNameTextView, priceTextView, descriptionTextView,
            averageRating, toltalNumComment, allCommentTextView;
    private ImageView subtractImageView, addImageView;
    private Button buttonS, buttonM, buttonL, buttonXL, buttonXXL, addProduct;
    private SessionManager sessionManager;
    private ProductDetailActivity productDetailActivity;
    private Dialog dialog;
    private NestedScrollView bottomSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;

    private RatingBar ratingBarAVE;
    private RecyclerView commentRCV;

    private ProductEntity productGlobal;
    private List<CommentEntity> list;
    private CommentAdapter commentAdapter;
    private int quantity = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        productDetailActivity = (ProductDetailActivity) getActivity();
        sessionManager = new SessionManager(productDetailActivity);
        initView();
        callApiGetAllCommentForProduct();
        return mView;
    }

    private void callApiGetAllCommentForProduct() {
        if (productGlobal != null) {
            if (sessionManager.isLoggedIn()) {
                String token = sessionManager.getJwt();
                String productCode = productGlobal.getProductCode();
                dialog = BaseActivity.openLoadingDialog(getContext());
                ApiService.apiService.getAllCommentForProduct(token, productCode)
                        .enqueue(new Callback<List<CommentEntity>>() {
                            @Override
                            public void onResponse(Call<List<CommentEntity>> call, Response<List<CommentEntity>> response) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                                list = response.body();
                                List<CommentEntity> listComment = new ArrayList<>();
                                int numComment = 3;
                                if (list.size() < 3) {
                                    numComment = list.size();
                                }
                                for (int i = 0; i < numComment; i++) {
                                    listComment.add(list.get(i));
                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                commentRCV.setLayoutManager(linearLayoutManager);
                                commentAdapter = new CommentAdapter(listComment);
                                commentRCV.setAdapter(commentAdapter);

                                int totalComment = 0;
                                double totalRating = 0;
                                for (CommentEntity c : list) {
                                    totalRating += c.getRating();
                                    totalComment += 1;
                                }
                                double aveRating = 0;
                                if (totalComment != 0) {
                                    aveRating = totalRating / totalComment;
                                }
                                DecimalFormat df = new DecimalFormat("#.#");
                                df.setRoundingMode(RoundingMode.HALF_UP);
                                aveRating = Double.parseDouble(df.format(aveRating));
                                ratingBarAVE.setRating((float) aveRating);

                                averageRating.setText(String.valueOf(aveRating));
                                toltalNumComment.setText(String.valueOf(totalComment) + " đánh giá");

                            }

                            @Override
                            public void onFailure(Call<List<CommentEntity>> call, Throwable throwable) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                BaseActivity.openErrorDialog(getContext(), "Không thể truy cập api");
                            }
                        });
            }
        }
    }

    private void initView() {
        quantityTextView = mView.findViewById(R.id.quantity);
        subtractImageView = mView.findViewById(R.id.subtract_quantity);
        addImageView = mView.findViewById(R.id.add_quantity);
        addProduct = mView.findViewById(R.id.add_product);
        productNameTextView = mView.findViewById(R.id.textView_product_name);
        priceTextView = mView.findViewById(R.id.textView_price);
        productColorView = mView.findViewById(R.id.view_product_color);
        descriptionTextView = mView.findViewById(R.id.decription);

        buttonS = mView.findViewById(R.id.button_s);
        buttonM = mView.findViewById(R.id.button_m);
        buttonL = mView.findViewById(R.id.button_l);
        buttonXL = mView.findViewById(R.id.button_xl);
        buttonXXL = mView.findViewById(R.id.button_xxl);

        bottomSheetLayout = mView.findViewById(R.id.bottomSheetLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        averageRating = mView.findViewById(R.id.averageRating);
        toltalNumComment = mView.findViewById(R.id.toltalNumComment);
        ratingBarAVE = mView.findViewById(R.id.ratingBarAVE);
        commentRCV = mView.findViewById(R.id.commentRCV);
        allCommentTextView = mView.findViewById(R.id.allCommentTextView);
        ratingBarAVE.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ffc922")));

        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonS.setSelected(true);
                buttonM.setSelected(false);
                buttonL.setSelected(false);
                buttonXL.setSelected(false);
                buttonXXL.setSelected(false);
            }
        });

        buttonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonS.setSelected(false);
                buttonM.setSelected(true);
                buttonL.setSelected(false);
                buttonXL.setSelected(false);
                buttonXXL.setSelected(false);
            }
        });

        buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonS.setSelected(false);
                buttonM.setSelected(false);
                buttonL.setSelected(true);
                buttonXL.setSelected(false);
                buttonXXL.setSelected(false);
            }
        });

        buttonXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonS.setSelected(false);
                buttonM.setSelected(false);
                buttonL.setSelected(false);
                buttonXL.setSelected(true);
                buttonXXL.setSelected(false);
            }
        });

        buttonXXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonS.setSelected(false);
                buttonM.setSelected(false);
                buttonL.setSelected(false);
                buttonXL.setSelected(false);
                buttonXXL.setSelected(true);
            }
        });

        quantityTextView.setText(String.valueOf(quantity));

        Bundle bundle = getArguments();
        if (bundle != null) {
            ProductEntity product = (ProductEntity) bundle.getSerializable("product");
            if (product != null) {
                productGlobal = product;
                productNameTextView.setText(product.getProductName());
                String priceString = String.format(Locale.getDefault(), "%.3fđ", product.getProductPrice());
                priceTextView.setText(priceString);


                // Xử lý sự kiện click vào dấu trừ
                subtractImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quantity > 1) {
                            quantity--;
                            quantityTextView.setText(String.valueOf(quantity));
                        }
                    }
                });

                // Xử lý sự kiện click vào dấu cộng
                addImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quantity + 1 <= product.getProductQuantity()) {
                            quantity++;
                            quantityTextView.setText(String.valueOf(quantity));
                        }
                    }
                });

                addProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String size = null;
                        if (buttonS.isSelected()) {
                            size = "S";
                        } else if (buttonM.isSelected()) {
                            size = "M";
                        } else if (buttonL.isSelected()) {
                            size = "L";
                        } else if (buttonXL.isSelected()) {
                            size = "XL";
                        } else if (buttonXXL.isSelected()) {
                            size = "XXL";
                        }
                        // Kiểm tra xem có kích thước nào được chọn không
                        if (size != null) {
                            callApiGetCartCode(product, size);
                        } else {
                            BaseActivity.openErrorDialog(getContext(), "Vui lòng chọn size!");
                        }

                    }
                });

//                String path = product.getImages().get(0);
//                String pathImage = "";
//                if (path != null && path.length() > 1) {
//                    String newPath = path.substring(1);
//                    pathImage = "http://10.0.2.2:8096"+newPath;
//                }
                //Glide.with(imageViewProduct).load(pathImage).into(imageViewProduct);

                viewPager = mView.findViewById(R.id.viewPager);
                indicator = mView.findViewById(R.id.indicator);
                photoProductAdapter = new PhotoProductAdapter(getContext(), getListPhoto(product.getImages()));
                indicator.setViewPager(viewPager);
                photoProductAdapter.registerDataSetObserver(indicator.getDataSetObserver());
                viewPager.setAdapter(photoProductAdapter);


                if (product.getProductColor().equals("red")) {
                    productColorView.setBackgroundResource(R.drawable.circle_background_red);
                } else if (product.getProductColor().equals("pink")) {
                    productColorView.setBackgroundResource(R.drawable.circle_background_pink);
                } else if (product.getProductColor().equals("yellow")) {
                    productColorView.setBackgroundResource(R.drawable.circle_background_yellow);
                } else if (product.getProductColor().equals("green")) {
                    productColorView.setBackgroundResource(R.drawable.circle_backgound_green);
                } else if (product.getProductColor().equals("blue")) {
                    productColorView.setBackgroundResource(R.drawable.circle_background_blue);
                } else if (product.getProductColor().equals("beige")) {
                    productColorView.setBackgroundResource(R.drawable.cirlce_background_beige);
                } else if (product.getProductColor().equals("white")) {
                    productColorView.setBackgroundResource(R.drawable.circle_background_white);
                } else if (product.getProductColor().equals("black")) {
                    productColorView.setBackgroundResource(R.drawable.circle_backgound_black);
                } else if (product.getProductColor().equals("brown")) {
                    productColorView.setBackgroundResource(R.drawable.circle_background_brown);
                } else if (product.getProductColor().equals("gray")) {
                    productColorView.setBackgroundResource(R.drawable.circle_background_gray);
                }

                // Đặt thông tin mô tả sản phẩm
                descriptionTextView.setText(product.getDescription());

                allCommentTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CommentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("productCode", product.getProductCode());
                        intent.putExtras(bundle); // Thêm dữ liệu để ẩn TextView
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private List<Photo> getListPhoto(List<String> images) {
        List<Photo> list = new ArrayList<>();
        for (String path : images) {
            String pathImage = "";
            if (path != null && path.length() > 1) {
                String newPath = path.substring(1);
                pathImage = "http://10.0.2.2:8096" + newPath;
            }
            list.add(new Photo(pathImage));
        }
        return list;
    }

    private void callApiGetCartCode(ProductEntity product, String size) {
        String email = sessionManager.getCustom("email");
        ApiService.apiService.findCartCode(sessionManager.getJwt(), email).enqueue(new Callback<CartCodeResponse>() {
            @Override
            public void onResponse(Call<CartCodeResponse> call, Response<CartCodeResponse> response) {
                if (response.isSuccessful()) {
                    CartCodeResponse temp = response.body();
                    String cartCode = temp.getCartCode();
                    callApiPushProduct(product, size, cartCode);
                } else {
                    BaseActivity.openErrorDialog(getContext(), "Không thể truy cập API.");
                }
            }

            @Override
            public void onFailure(Call<CartCodeResponse> call, Throwable throwable) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                BaseActivity.openErrorDialog(getContext(), "Không thể truy cập API, vui lòng thử lại sau!");

            }
        });
    }

    private void callApiPushProduct(ProductEntity product, String size, String cartCode) {
        Dialog dg = BaseActivity.openLoadingDialog(getContext());
        ApiService.apiService.AddProduct(sessionManager.getJwt(), cartCode,
                product.getProductCode(), quantity, size, quantity * product.getProductPrice()).enqueue(new Callback<BooleanResponse>() {
            @Override
            public void onResponse(Call<BooleanResponse> call, Response<BooleanResponse> response) {
                dg.dismiss();
                if (response.isSuccessful()) {
                    BooleanResponse result = response.body();
                    if ("done".equals(result.getSuccess())) {
                        productDetailActivity.setCartBadge(productDetailActivity.getCartBadge() + 1);
                        BaseActivity.openSuccessDialog(getContext(), "Thêm sản phẩm thành công!");
                        // Đặt lại kích thước và số lượng mặc định
                        resetDefaultSizeAndQuantity();
                    }
                } else {
                    BaseActivity.openErrorDialog(getContext(), "Xảy ra lỗi!");
                }
            }

            @Override
            public void onFailure(Call<BooleanResponse> call, Throwable throwable) {
                if (dg != null && dg.isShowing()) {
                    dg.dismiss();
                }
                BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
            }
        });

    }

    private void resetDefaultSizeAndQuantity() {
        // Đặt lại kích thước mặc định
        buttonS.setSelected(false);
        buttonM.setSelected(false);
        buttonL.setSelected(false);
        buttonXL.setSelected(false);
        buttonXXL.setSelected(false);

        // Đặt lại số lượng mặc định
        quantity = 1;
        quantityTextView.setText(String.valueOf(quantity));
    }
}
