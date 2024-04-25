package com.example.clothingstoreapp.fragment.fragmenOfBaseActivity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.BaseActivity;
import com.example.clothingstoreapp.adapter.categorylistview.CategoryListviewAdapter;
import com.example.clothingstoreapp.adapter.categorylistview.ItemModel;
import com.example.clothingstoreapp.api.ApiService;
import com.example.clothingstoreapp.custom_interface.IClickCategoryListener;
import com.example.clothingstoreapp.entity.CategoryEntity;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private BaseActivity baseActivity;
    private Dialog dialog;
    private List<CategoryEntity> listCategories;
    private View mView;
    private Button femaleButton, maleButton;
    private CategoryListviewAdapter categoryListviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_product, container, false);

        categoryListviewAdapter = new CategoryListviewAdapter(new ArrayList<>(), new IClickCategoryListener() {
            @Override
            public void onClickChoose(CategoryEntity itemModel) {
                Bundle bundle = new Bundle();
                bundle.putInt("category_id", itemModel.getId());

                Fragment fragment = new ListProductFragment();
                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        RecyclerView mRecyclerView = mView.findViewById(R.id.rv_Category);

        listCategories = new ArrayList<>();
        dialog = BaseActivity.openLoadingDialog(getContext());

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(categoryListviewAdapter);

        femaleButton = mView.findViewById(R.id.femaleButton);
        maleButton = mView.findViewById(R.id.maleButton);

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataForFemaleTab();
            }
        });

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataForMaleTab();
            }
        });
        callApiGetCategories();
        return mView;
    }

    // Hàm update dữ liệu cho tab Nữ
    private void updateDataForFemaleTab() {
        if (listCategories != null && !listCategories.isEmpty()) {
            List<ItemModel> data = new ArrayList<>();
            int tempAo = 0, tempQuan = 0;
            for (CategoryEntity category : listCategories) {
                if ("Áo nữ".equals(category.getCategoryParent().getCategoryName())) {
                    if (tempAo == 0) {
                        data.add(new ItemModel("ÁO", ItemModel.PARENT_TYPE, category));
                        tempAo++;
                    }
                    data.add(new ItemModel(category.getCategoryName(), ItemModel.CHILD_TYPE, category));
                }
                if ("Quần nữ".equals(category.getCategoryParent().getCategoryName())) {
                    if (tempQuan == 0) {
                        data.add(new ItemModel("QUẦN", ItemModel.PARENT_TYPE, category));
                        tempQuan++;
                    }
                    data.add(new ItemModel(category.getCategoryName(), ItemModel.CHILD_TYPE, category));
                }
            }
            // Cập nhật dữ liệu cho RecyclerView
            categoryListviewAdapter.updateData(data);
        }
        femaleButton.setEnabled(false);
        maleButton.setEnabled(true);
        maleButton.setTextColor(getResources().getColor(R.color.black, null));
        femaleButton.setTextColor(getResources().getColor(R.color.unselected_button, null));

    }

    // Hàm update dữ liệu cho tab Nam
    private void updateDataForMaleTab() {
        if (listCategories != null && !listCategories.isEmpty()) {
            List<ItemModel> data = new ArrayList<>();
            int tempAo = 0, tempQuan = 0;
            for (CategoryEntity category : listCategories) {
                if ("Áo nam".equals(category.getCategoryParent().getCategoryName())) {
                    if (tempAo == 0) {
                        data.add(new ItemModel("ÁO", ItemModel.PARENT_TYPE, category));
                        tempAo++;
                    }
                    data.add(new ItemModel(category.getCategoryName(), ItemModel.CHILD_TYPE, category));
                }
                if ("Quần nam".equals(category.getCategoryParent().getCategoryName())) {
                    if (tempQuan == 0) {
                        data.add(new ItemModel("QUẦN", ItemModel.PARENT_TYPE, category));
                        tempQuan++;
                    }
                    data.add(new ItemModel(category.getCategoryName(), ItemModel.CHILD_TYPE, category));
                }
            }
            // Cập nhật dữ liệu cho RecyclerView
            categoryListviewAdapter.updateData(data);
        }
        femaleButton.setEnabled(true);
        maleButton.setEnabled(false);
        femaleButton.setTextColor(getResources().getColor(R.color.black, null));
        maleButton.setTextColor(getResources().getColor(R.color.unselected_button, null));
    }

    // Hàm gọi API để lấy danh sách danh mục
    private void callApiGetCategories() {
        ApiService.apiService.getAllCategories().enqueue(new Callback<List<CategoryEntity>>() {
            @Override
            public void onResponse(Call<List<CategoryEntity>> call, Response<List<CategoryEntity>> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    listCategories = response.body();
                    // Cập nhật dữ liệu cho tab Nữ
                    updateDataForFemaleTab();
                } else {
                    BaseActivity.openErrorDialog(getContext(), "Không thể lấy danh sách danh mục từ API.");
                }

            }

            @Override
            public void onFailure(Call<List<CategoryEntity>> call, Throwable throwable) {
                // Xử lý lỗi
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                BaseActivity.openErrorDialog(getContext(), "Hiện không thể truy cập API, vui lòng thử lại sau!");
            }
        });
    }

}

