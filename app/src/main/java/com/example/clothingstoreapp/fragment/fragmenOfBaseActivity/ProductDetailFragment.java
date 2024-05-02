package com.example.clothingstoreapp.fragment.fragmenOfBaseActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.ListProductActivity;
import com.example.clothingstoreapp.activity.ProductDetailActivity;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.entity.ProductEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.BooleanResponse;
import com.example.clothingstoreapp.response.CartCodeResponse;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailFragment extends Fragment {
    private View mView, productColorView;
    private TextView quantityTextView,productNameTextView, priceTextView, descriptionTextView;
    private ImageView subtractImageView, addImageView, imageViewProduct;
    private Button buttonS, buttonM, buttonL, buttonXL, buttonXXL, addProduct;
    private SessionManager sessionManager;
    private ProductDetailActivity productDetailActivity;
    private Dialog dialog;

    private int quantity = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        productDetailActivity = (ProductDetailActivity) getActivity();
        sessionManager = new SessionManager(productDetailActivity);
        initView();

        return mView;
    }

    private void initView() {
        quantityTextView = mView.findViewById(R.id.quantity);
        subtractImageView = mView.findViewById(R.id.subtract_quantity);
        addImageView = mView.findViewById(R.id.add_quantity);
        addProduct = mView.findViewById(R.id.add_product);
        imageViewProduct = mView.findViewById(R.id.image_product);
        productNameTextView = mView.findViewById(R.id.textView_product_name);
        priceTextView = mView.findViewById(R.id.textView_price);
        productColorView = mView.findViewById(R.id.view_product_color);
        descriptionTextView = mView.findViewById(R.id.decription);

        buttonS = mView.findViewById(R.id.button_s);
        buttonM = mView.findViewById(R.id.button_m);
        buttonL = mView.findViewById(R.id.button_l);
        buttonXL = mView.findViewById(R.id.button_xl);
        buttonXXL = mView.findViewById(R.id.button_xxl);

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
                            BaseActivity.openErrorDialog(getContext(),"Vui lòng chọn size!");
                        }

                    }
                });

                String path = product.getImages().get(0);
                String pathImage = "";
                if (path != null && path.length() > 1) {
                    String newPath = path.substring(1);
                    pathImage = "http://10.0.2.2:8096"+newPath;
                }
                Glide.with(imageViewProduct).load(pathImage).into(imageViewProduct);
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
            }
        }
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
                    if ("done".equals(result.getSuccess())){
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
