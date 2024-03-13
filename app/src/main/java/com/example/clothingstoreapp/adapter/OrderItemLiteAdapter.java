package com.example.clothingstoreapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.OrderManagementActivity;
import com.example.clothingstoreapp.entity.OrderItemEntity;

import java.util.List;

public class OrderItemLiteAdapter extends RecyclerView.Adapter<OrderItemLiteAdapter.OrderItemViewHolder> implements Filterable {

    private OrderManagementActivity orderManagementActivity;
    private List<OrderItemEntity> list;
    @Override
    public Filter getFilter() {
        return null;
    }

    public OrderItemLiteAdapter(List<OrderItemEntity> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail, parent, false);
        return new OrderItemLiteAdapter.OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItemEntity orderItemEntity = list.get(position);

        if (orderItemEntity == null) {
            return;
        } else {
            holder.productNameTextView.setText(orderItemEntity.getProduct().getProductName());
            holder.quantityTextView.setText(String.valueOf(orderItemEntity.getQuantity()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private TextView quantityTextView, productNameTextView;
        private RecyclerView productRecyclerView;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
        }
    }
}
