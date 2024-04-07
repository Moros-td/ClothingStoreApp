package com.example.clothingstoreapp.fragment.fragmenOfBaseActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AuthenticationActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.adapter.ProductAdapter;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.custom_interface.IClickItemProductListener;
import com.example.clothingstoreapp.entity.ProductEntity;
import com.example.clothingstoreapp.interceptor.SessionManager;
import com.example.clothingstoreapp.response.ProductResponse;
import com.example.clothingstoreapp.response.CartCodeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private View mView;

    private Dialog dialog;

    private List<ProductEntity> listProducts;

    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = mView.findViewById(R.id.searchViewProduct);
        baseActivity = (BaseActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_product);
        sessionManager = new SessionManager(baseActivity);
        //set layout manager cho rcv
        GridLayoutManager gridLayoutManager = new GridLayoutManager(baseActivity, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        listProducts = new ArrayList<>();
        dialog = BaseActivity.openLoadingDialog(getContext());
        // set adapter cho rcv
        callApiGetProducts();

        return mView;
    }

    private void callApiGetProducts() {
        ApiService.apiService.getAllProducts().enqueue(new Callback<List<ProductEntity>>() {
            @Override
            public void onResponse(Call<List<ProductEntity>> call, Response<List<ProductEntity>> response) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                listProducts = response.body();
                ProductAdapter productAdapter = new ProductAdapter(listProducts, new IClickItemProductListener() {
                    @Override
                    public void onClickAddProduct(ProductEntity product) {
                        // sự kiện nút add sản phẩm
                        String token = sessionManager.getJwt();
                        if (token != null)
                            openConfirmSizeDialog(product);
                        else {
                            Intent authenticationActivity = new Intent(getContext(), AuthenticationActivity.class);

                            startActivity(authenticationActivity);
                        }
                    }
                });
                recyclerView.setAdapter(productAdapter);

                // sự kiện cho search view
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        productAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        productAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
            }

            @Override
            public void onFailure(Call<List<ProductEntity>> call, Throwable throwable) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập api, vui lòng thử lại sau!");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void openConfirmSizeDialog(ProductEntity product) {
        final Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_size_dialog);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttr = window.getAttributes();
        windowAttr.gravity = Gravity.CENTER;
        window.setAttributes(windowAttr);

        // click ra ngoài thì tắt dialog
        dialog.setCancelable(true);

        //setView trên dialog
        TextView sizeS = dialog.findViewById(R.id.size_S);
        TextView sizeM = dialog.findViewById(R.id.size_M);
        TextView sizeL = dialog.findViewById(R.id.size_L);
        TextView sizeXL = dialog.findViewById(R.id.size_XL);
        TextView sizeXXL = dialog.findViewById(R.id.size_XXL);

        Map<String, Integer> sizes = product.getSizes();
        if (sizes.get("S") != null) {
            if (sizes.get("S") > 1) {
                sizeS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        callApiGetCartCode(product, "S");
                    }
                });
            } else {
                sizeS.setBackgroundColor(getResources().getColor(R.color.disabled_color, null));
                //sizeS.setBackgroundColor(ContextCompat.getColor(baseActivity, R.color.disabled_color));
            }
        }

        if (sizes.get("M") != null) {
            if (sizes.get("M") > 1) {
                sizeM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        callApiGetCartCode(product, "M");
                    }
                });
            } else {
                sizeM.setBackgroundColor(getResources().getColor(R.color.disabled_color, null));
            }
        }

        if (sizes.get("L") != null) {
            if (sizes.get("L") > 1) {
                sizeL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        callApiGetCartCode(product, "L");
                    }
                });
            } else {
                sizeL.setBackgroundColor(getResources().getColor(R.color.disabled_color, null));
            }
        }

        if (sizes.get("XL") != null) {
            if (sizes.get("XL") > 1) {
                sizeXL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        callApiGetCartCode(product, "XL");
                    }
                });
            } else {
                sizeXL.setBackgroundColor(getResources().getColor(R.color.disabled_color, null));
            }
        }

        if (sizes.get("XXL") != null) {
            if (sizes.get("XXL") > 1) {
                sizeXXL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        callApiGetCartCode(product, "XXL");
                    }
                });
            } else {
                sizeXXL.setBackgroundColor(getResources().getColor(R.color.disabled_color, null));
            }
        }

        dialog.show();
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
                product.getProductCode(), 1, size, product.getProductPrice()).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                dg.dismiss();
                if (response.isSuccessful()) {
                    ProductResponse result = response.body();
                    if ("done".equals(result.getSuccess()))
                        BaseActivity.openSuccessDialog(getContext(), "Thêm sản phẩm thành công!");
                } else {
                    BaseActivity.openErrorDialog(getContext(), "Xảy ra lỗi!");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable throwable) {
                if (dg != null && dg.isShowing()) {
                    dg.dismiss();
                }
                BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
            }
        });
    }


//    public void moveData(ProductEntity product, String size){
//        Intent intent = new Intent(getActivity(), CartBaseActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("size",size);
//        bundle.putSerializable("product_enity", product);
//        //bundle.putBoolean("hideCategoryId", false);
//        intent.putExtras(bundle); // Thêm dữ liệu để ẩn TextView
//        startActivity(intent);
//
//    }
}
