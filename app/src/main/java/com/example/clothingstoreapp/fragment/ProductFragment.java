package com.example.clothingstoreapp.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.adapter.ProductAdapter;
import com.example.clothingstoreapp.entity.CategoryEntity;
import com.example.clothingstoreapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_product, container, false);
        return mView;
    }

    private List<CategoryEntity> getListCategory() {
        List<CategoryEntity> list = new ArrayList<>();
        list.add(new CategoryEntity(1,"Áo nữ",  null));
        list.add(new CategoryEntity(2,"Áo nam",  null));
        list.add(new CategoryEntity(3,"Quần nữ",  null));
        list.add(new CategoryEntity(4,"Quần nam",  null));
        list.add(new CategoryEntity(5,"Áo sơ mi nữ", new CategoryEntity(1, "Áo nữ", null)));
        list.add(new CategoryEntity(6, "Áo thun nam", new CategoryEntity(2, "Áo nam", null)));
        list.add(new CategoryEntity(7, "Quần jean nam", new CategoryEntity(4, "Quần nam", null)));
        list.add(new CategoryEntity(8, "Quần ống loe", new CategoryEntity(3, "Quần nữ", null)));
        return list;
    }
}