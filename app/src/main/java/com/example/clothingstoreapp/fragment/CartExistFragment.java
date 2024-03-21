package com.example.clothingstoreapp.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.example.clothingstoreapp.adapter.CartAdapter;
import com.example.clothingstoreapp.custom_interface.IClickItemCartListener;
import com.example.clothingstoreapp.custom_interface.IClickItemProductListener;
import com.example.clothingstoreapp.entity.CartItemEnity;
import com.example.clothingstoreapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class CartExistFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartBaseActivity cartBaseActivity;
    private View mView;
    private Button btnOrder;
    private List<CartItemEnity> list;
    private IClickItemProductListener iClickItemProductListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_cart_exist, container, false);
        cartBaseActivity = (CartBaseActivity) getActivity();
        recyclerView = mView.findViewById(R.id.rcv_product_cart);

        //set layout manager cho rcv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cartBaseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Ánh xạ TextView của footer
        TextView totalPriceTextView = mView.findViewById(R.id.cart_totalPrice);
        TextView tempPriceTextView = mView.findViewById(R.id.cart_tempPrice);

        // Set adapter cho recyclerView và truyền các TextView của footer vào
        list = getListProductCart();
        CartAdapter cartAdapter = new CartAdapter(list, totalPriceTextView, tempPriceTextView, new IClickItemCartListener() {
            @Override
            public void onClickRemove(CartItemEnity cartItemEnity) {
                openConfirmDialog(cartItemEnity.getProduct());
            }

            @Override
            public void onClickSubtract(CartItemEnity cartItem) {
            }

            @Override
            public void onClickAdd(CartItemEnity cartItem) {
            }
        });

        //set adapter
        recyclerView.setAdapter(cartAdapter);

        // Gọi phương thức để cập nhật footer total
        footerTotal();
        //chuyển đến trang địa chỉ
        btnOrder = mView.findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CartAddressFragment();
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerCart, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return mView;
    }

    private List<CartItemEnity> getListProductCart() {
        List<CartItemEnity> list = new ArrayList<>();
        ProductEntity product = new ProductEntity("SP123", "ÁO THUN TRƠN CỔ ĐỨC KHUY NGỌC TRAI ", 5, 500.00);
        list.add(new CartItemEnity("0001", product, 2, "XL", 2 * product.getProductPrice()));
        list.add(new CartItemEnity("0002", product, 4, "L", 4 * product.getProductPrice()));
        return list;
    }

    private void footerTotal() {
        CartAdapter cartAdapter = (CartAdapter) recyclerView.getAdapter();
        if (cartAdapter != null) {
            cartAdapter.footerTotal();
        }
    }

    private void openConfirmDialog(ProductEntity product) {
        final Dialog dialog = new Dialog(cartBaseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog);

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

        // setView trên dialog
        TextView confirmTextView = dialog.findViewById(R.id.confirmTextView);
        Button deleteConfirmBtn = dialog.findViewById(R.id.deleteConfirmBtn);
        Button cancelConfirmBtn = dialog.findViewById(R.id.cancleConfirmBtn);

        confirmTextView.setText("Bạn có chắn rằng muốn xóa sản phẩm này không?");

        //set event
        deleteConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                BaseActivity.openSuccessDialog(cartBaseActivity, "Xóa sản phẩm thành công!");
            }
        });

        cancelConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}