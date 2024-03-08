package com.example.clothingstoreapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.example.clothingstoreapp.adapter.ProductAdapter;
import com.example.clothingstoreapp.custom_interface.IClickItemProductListener;
import com.example.clothingstoreapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = mView.findViewById(R.id.searchViewProduct);
        baseActivity = (BaseActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_product);

        //set layout manager cho rcv
        GridLayoutManager gridLayoutManager = new GridLayoutManager(baseActivity, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // set adapter cho rcv
        ProductAdapter productAdapter = new ProductAdapter(getListProduct(), new IClickItemProductListener() {
            @Override
            public void onClickAddProduct(ProductEntity product) {
                // sự kiện nút add sản phẩm
                openConfirmSizeDialog(product);
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

        return mView;
    }

    private List<ProductEntity> getListProduct() {
        List<ProductEntity> listP = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listP.add(new ProductEntity("SP123", "ÁO THUN TRƠN CỔ ĐỨC KHUY NGỌC TRAI " + i, 50, 500.00));
        }
        return listP;
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

        //set sự kiện chọn size
        sizeS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openSuccessDialog();
            }
        });
        sizeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openSuccessDialog();
            }
        });
        sizeL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openSuccessDialog();
            }
        });
        sizeXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openSuccessDialog();
            }
        });
        sizeXXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openSuccessDialog();
            }
        });

        dialog.show();
    }

    private void openSuccessDialog() {
        final Dialog dialog = new Dialog(baseActivity);
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

        successTextView.setText("Thêm sản phẩm thành công!");

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
