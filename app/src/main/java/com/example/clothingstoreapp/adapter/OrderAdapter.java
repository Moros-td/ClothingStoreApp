package com.example.clothingstoreapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.OrderManagementActivity;
import com.example.clothingstoreapp.custom_interface.IClickItemOrderListener;
import com.example.clothingstoreapp.entity.OrderEntity;
import com.example.clothingstoreapp.entity.OrderItemEntity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements Filterable {

    private OrderManagementActivity orderManagementActivity;
    private IClickItemOrderListener iClickItemOrderListener;
    private List<OrderEntity> list;

    @Override
    public Filter getFilter() {
        return null;
    }

    public OrderAdapter(List<OrderEntity> list, IClickItemOrderListener listener) {
        this.list = list;
        iClickItemOrderListener = listener;
    }



    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderEntity orderEntity = list.get(position);

        if (orderEntity == null) {
            return;
        } else {
            holder.orderCodeTextView.setText(orderEntity.getOrderCode());
            holder.orderDateTextView.setText(String.valueOf(orderEntity.getOrderDate()));

            if(orderEntity.getOrderState().equals("pending")){
                holder.orderStateTextView.setText("Đang xử lý");
                holder.orderStateTextView.setTextColor(Color.parseColor("#f5e642"));
            }
            else if(orderEntity.getOrderState().equals("delivering")){
                holder.orderStateTextView.setText("Đang vận chuyển");
                holder.orderStateTextView.setTextColor(Color.parseColor("#2933f0"));
            }
            else if(orderEntity.getOrderState().equals("cancelled")){
                holder.orderStateTextView.setText("Đã hủy");
                holder.orderStateTextView.setTextColor(Color.parseColor("#ab1a13"));
            }
            else{
                holder.orderStateTextView.setText("Giao hàng thành công");
                holder.orderStateTextView.setTextColor(Color.parseColor("#42ad2a"));
            }
            holder.totalOrderPriceTextView.setText(String.valueOf(orderEntity.getTotalPrice()));

            List<OrderItemEntity> listOrderItem = orderEntity.getListOrderItem();

            OrderItemLiteAdapter orderItemLiteAdapter = new OrderItemLiteAdapter(listOrderItem);
            holder.productRecyclerView.setLayoutManager(new LinearLayoutManager(orderManagementActivity));
            holder.productRecyclerView.setAdapter(orderItemLiteAdapter);

            if(!"delivered".equals(orderEntity.getOrderState())){
                holder.notifyTextView.setVisibility(View.GONE);
            }else{
                List<OrderItemEntity> OIList = orderEntity.getListOrderItem();
                for (OrderItemEntity x:OIList) {
                    if(x.getCommentState() == 0){
                        holder.notifyTextView.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            if(iClickItemOrderListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemOrderListener.onClickOrder(orderEntity);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderCodeTextView, orderStateTextView, orderDateTextView, totalOrderPriceTextView,
                notifyTextView;
        private RecyclerView productRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCodeTextView = itemView.findViewById(R.id.orderCodeTextView);
            orderStateTextView = itemView.findViewById(R.id.orderStateTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            totalOrderPriceTextView = itemView.findViewById(R.id.totalOrderPriceTextView);
            productRecyclerView = itemView.findViewById(R.id.rcv_product_in_order);
            notifyTextView = itemView.findViewById(R.id.notifyTextView);
        }
    }
}
