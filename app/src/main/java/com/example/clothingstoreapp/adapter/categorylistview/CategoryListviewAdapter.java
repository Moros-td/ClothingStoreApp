package com.example.clothingstoreapp.adapter.categorylistview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;

import java.util.List;

public class CategoryListviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemModel> mList;
    public CategoryListviewAdapter(List<ItemModel> list) {
        this.mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        if(mList!=null)
        {
            ItemModel object = mList.get(position);
            if (object != null) {
                return object.getmType();
            }
        }
        return 0;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case ItemModel.PARENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
                return new CategoryGroup(view);

            case ItemModel.CHILD_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
                return new CategoryDetail(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemModel object = mList.get(position);
        if (object != null) {
            switch (object.getmType()) {
                case ItemModel.PARENT_TYPE:
                    ((CategoryGroup) holder).mTitle.setText(object.getCategoryname());
                    break;
                case ItemModel.CHILD_TYPE:
                    ((CategoryDetail) holder).mTitle.setText(object.getCategoryname());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList==null){
            return 0;
        }
        return mList.size();
    }

    // viewHolder category parent
    public static class CategoryGroup extends RecyclerView.ViewHolder{
        private TextView mTitle;
        public CategoryGroup(@NonNull View itemView) {
            super(itemView);

            mTitle=itemView.findViewById(R.id.categoryName);
        }
    }

    // viewHolder category child
    public static class CategoryDetail extends RecyclerView.ViewHolder{
        private TextView mTitle;
        public CategoryDetail(@NonNull View itemView) {
            super(itemView);

            mTitle=itemView.findViewById(R.id.itemName);

        }
    }
    public void updateData(List<ItemModel> newData) {
        mList.clear();
        mList.addAll(newData);
        notifyDataSetChanged();
    }

}
