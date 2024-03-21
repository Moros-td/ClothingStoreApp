package com.example.clothingstoreapp.fragment.fragmentOfAddressManagementActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AddAddressActivity;
import com.example.clothingstoreapp.activity.AddressManagementActivity;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.activity.CartBaseActivity;
import com.example.clothingstoreapp.adapter.AddressAdapter;
import com.example.clothingstoreapp.adapter.AddressManagementAdapter;
import com.example.clothingstoreapp.custom_interface.IClickItemAddress;
import com.example.clothingstoreapp.custom_interface.IClickItemEditAddressListener;
import com.example.clothingstoreapp.entity.AddressEntity;

import java.util.ArrayList;
import java.util.List;

public class AddressManagementFragment extends Fragment {
    private RecyclerView recyclerView;
    private View mView;
    private Button btnOrder;
    private List<AddressEntity> list;
    private AddressManagementActivity addressManagementActivity;
    private AddressManagementAdapter addressManagementAdapter;
    private IClickItemEditAddressListener iClickItemEditAddressListener;
    public AddressManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_address_management, container, false);
        addressManagementActivity = (AddressManagementActivity) getContext();

        recyclerView = mView.findViewById(R.id.rcv_address_management);
        list = getListAddress();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(addressManagementActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        addressManagementAdapter = new AddressManagementAdapter(list, new IClickItemEditAddressListener() {
            @Override
            public void onClickEditAddress(AddressEntity address) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("address", address.getAddress());
                addressManagementActivity.openFragment(AddressManagementActivity.FRAGMENT_ADDRESS_INFO,bundle);
            }
        });

        recyclerView.setAdapter(addressManagementAdapter);

        LinearLayout layout = mView.findViewById(R.id.layout_add_address_mg); // ánh xạ layout từ layout của Fragment
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressManagementActivity.openFragment(AddressManagementActivity.FRAGMENT_ADDRESS_INFO,null);
            }
        });

        return mView;
    }

    private List<AddressEntity> getListAddress() {
        List<AddressEntity> list = new ArrayList<>();
        list.add(new AddressEntity("Văn Tèo", "0912333333", "97 Man Thiện, Phường Đa Kao, Quận 1, Hồ Chí Minh"));
        list.add(new AddressEntity("Văn Tèo", "0912333333", "12/1, Phường Kim Mã, Quận Ba Đình, Hà Nội"));
        return list;
    }

}