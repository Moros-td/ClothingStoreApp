package com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AddAddressActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.example.clothingstoreapp.adapter.AddressAdapter;
import com.example.clothingstoreapp.custom_interface.IClickItemAddress;
import com.example.clothingstoreapp.entity.AddressEntity;
import com.example.clothingstoreapp.fragment.fragmentOfCartBaseActivity.CartSuccessFragment;

import java.util.ArrayList;
import java.util.List;

public class CartAddressFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartBaseActivity cartBaseActivity;
    private View mView;
    private Button btnOrder;
    private List<AddressEntity> list;
    private IClickItemAddress iClickItemAddress;
    private RadioButton radioButton;
    private AddressAdapter addressAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_cart_address, container, false);

        cartBaseActivity = (CartBaseActivity) getActivity();

        recyclerView = mView.findViewById(R.id.rcv_address_cart);
        list = getListAddress();

        // Initialize listener
        iClickItemAddress = new IClickItemAddress() {
            @Override
            public void onClickChoose(AddressEntity addressEntity) {
                // Notify adapter
                recyclerView.post(new Runnable() {
                    @Override public void run()
                    {
                        addressAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cartBaseActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        addressAdapter = new AddressAdapter(list, iClickItemAddress);
        recyclerView.setAdapter(addressAdapter);
        btnOrder = mView.findViewById(R.id.btn_address_cart);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CartSuccessFragment();
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.containerCart, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        LinearLayout layout = mView.findViewById(R.id.layout_add_address); // ánh xạ layout từ layout của Fragment
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent để chuyển từ Fragment sang Activity mới
                Intent intent = new Intent(getActivity(), AddAddressActivity.class);

                // Bắt đầu Activity mới bằng Intent
                startActivity(intent);
            }
        });


        // Ánh xạ TextView của footer
//        TextView totalPriceTextView = mView.findViewById(R.id.cart_totalPrice);
//        TextView tempPriceTextView = mView.findViewById(R.id.cart_tempPrice);
        return mView;
    }

    private List<AddressEntity> getListAddress() {
        List<AddressEntity> list = new ArrayList<>();
        list.add(new AddressEntity("Văn Tèo", "0912333333", "97, Man Thiện, Phường Tăng Nhơn Phú A, Thành phố Thủ Đức, Hồ Chí Minh"));
        list.add(new AddressEntity("Văn Tèo", "0912333333", "Hồ Chí Minh"));
        return list;
    }
}
