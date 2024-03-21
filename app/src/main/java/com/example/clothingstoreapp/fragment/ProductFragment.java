package com.example.clothingstoreapp.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.adapter.categorylistview.CategoryListviewAdapter;
import com.example.clothingstoreapp.adapter.categorylistview.ItemModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private ListView listView;
    private View mView;
    private Button femaleButton, maleButton;
    private CategoryListviewAdapter categoryListviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_product, container, false);

        CategoryListviewAdapter categoryListviewAdapter = new CategoryListviewAdapter(getDummyData("NỮ")); // Khởi tạo với dữ liệu cho NỮ ban đầu
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView mRecyclerView = mView.findViewById(R.id.rv_Category);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(categoryListviewAdapter);

        // Xử lý sự kiện click cho nút NỮ
        femaleButton = mView.findViewById(R.id.femaleButton);
        maleButton = mView.findViewById(R.id.maleButton);
        femaleButton.setEnabled(false);
        maleButton.setEnabled(true);
        maleButton.setTextColor(getResources().getColor(R.color.black,null));
        femaleButton.setTextColor(getResources().getColor(R.color.unselected_button,null));

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListviewAdapter.updateData(getDummyData("NỮ")); // Cập nhật dữ liệu cho NỮ
                maleButton.setEnabled(true);
                femaleButton.setEnabled(false);
                maleButton.setTextColor(getResources().getColor(R.color.black,null));
                femaleButton.setTextColor(getResources().getColor(R.color.unselected_button,null));
            }
        });

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListviewAdapter.updateData(getDummyData("NAM")); // Cập nhật dữ liệu cho NAM
                femaleButton.setEnabled(true);
                maleButton.setEnabled(false);
                femaleButton.setTextColor(getResources().getColor(R.color.black,null));
                maleButton.setTextColor(getResources().getColor(R.color.unselected_button,null));
            }
        });

        return mView;
    }
    public static List<ItemModel> getDummyData(String gender) {
        List<ItemModel> list = new ArrayList<>();
        list.add(new ItemModel("ÁO", ItemModel.PARENT_TYPE));
        if (gender.equals("NỮ")) {
            list.add(new ItemModel("Áo sơ mi nữ", ItemModel.CHILD_TYPE));
            list.add(new ItemModel("Áo thun nữ", ItemModel.CHILD_TYPE));
            list.add(new ItemModel("Áo khoác nữ", ItemModel.CHILD_TYPE));
            list.add(new ItemModel("Áo len nữ", ItemModel.CHILD_TYPE));
        } else if (gender.equals("NAM")) {
            list.add(new ItemModel("Áo sơ mi nam", ItemModel.CHILD_TYPE));
            list.add(new ItemModel("Áo thun nam", ItemModel.CHILD_TYPE));
            list.add(new ItemModel("Áo khoác nam", ItemModel.CHILD_TYPE));
            list.add(new ItemModel("Áo len nam", ItemModel.CHILD_TYPE));
        }
        list.add(new ItemModel("QUẦN", ItemModel.PARENT_TYPE));
        if (gender.equals("NỮ")) {
            list.add(new ItemModel("Quần ống loe nữ", ItemModel.CHILD_TYPE));
            list.add(new ItemModel("Quần sort nữ", ItemModel.CHILD_TYPE));
        } else if (gender.equals("NAM")) {
            list.add(new ItemModel("Quần tây nam", ItemModel.CHILD_TYPE));
            list.add(new ItemModel("Quần sort nam", ItemModel.CHILD_TYPE));
        }
        return list;
    }
}

