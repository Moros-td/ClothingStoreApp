package com.example.clothingstoreapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.entity.OrderItemEntity;

import java.util.List;

public class OrderItemFullInfoAdapter extends RecyclerView.Adapter<OrderItemFullInfoAdapter.ProductInOrderViewHolder> implements Filterable {
    private List<OrderItemEntity> list;
    @Override
    public Filter getFilter() {
        return null;
    }

    public OrderItemFullInfoAdapter(List<OrderItemEntity> list) {
        this.list = list;
    }



    @NonNull
    @Override
    public ProductInOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_full_info, parent, false);
        return new OrderItemFullInfoAdapter.ProductInOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductInOrderViewHolder holder, int position) {
        OrderItemEntity orderItemEntity  = list.get(position);
        if (orderItemEntity == null) {
            return;
        } else {
            holder.productNameTextView.setText(orderItemEntity.getProduct().getProductName());
            holder.quantityTextView.setText(String.valueOf(orderItemEntity.getQuantity()));
            holder.sizeTextView.setText(orderItemEntity.getSize());
            holder.totalPriceTextView.setText(String.valueOf(orderItemEntity.getTotalPrice()));

            // sự kiện các nút
//            if(iClickItemCartListener != null){
//            }
        }
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public class ProductInOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView
                productNameTextView, quantityTextView, sizeTextView, totalPriceTextView;
        private ImageView productImageView;

        public ProductInOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            sizeTextView = itemView.findViewById(R.id.sizeTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
        }
    }
}
