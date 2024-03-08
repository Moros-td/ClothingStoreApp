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
import com.example.clothingstoreapp.custom_interface.IClickItemCartListener;
import com.example.clothingstoreapp.custom_interface.IClickItemProductListener;
import com.example.clothingstoreapp.entity.CartItemEnity;
import com.example.clothingstoreapp.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> implements Filterable {

    private List<CartItemEnity> list;
    private TextView textViewTotalPrice, textViewTempPrice;
    private IClickItemCartListener iClickItemCartListener;
    double total;

    public CartAdapter(List<CartItemEnity> list, TextView totalPriceTextView, TextView tempPriceTextView, IClickItemCartListener listener) {
        this.list = list;
        this.textViewTotalPrice = totalPriceTextView;
        this.textViewTempPrice = tempPriceTextView;
        this.iClickItemCartListener = listener;
        total = 0;
        for (CartItemEnity item : list) {
            total += item.getTotalPrice();
        }
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemEnity cartItemEntity = list.get(position);
        if (cartItemEntity == null) {
            return;
        } else {
            holder.textViewSize.setText(cartItemEntity.getSize());
            holder.textViewQuantity.setText(String.valueOf(cartItemEntity.getQuantity()));
            holder.textViewProductPrice.setText(String.valueOf(cartItemEntity.getTotalPrice()));

            // sự kiện các nút
            if(iClickItemCartListener != null){
                holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemCartListener.onClickRemove(cartItemEntity);
                    }
                });
                holder.imageViewSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemCartListener.onClickSubtract(cartItemEntity);
                        int newQuantity = cartItemEntity.getQuantity() - 1;

                        if (newQuantity > 0) {
                            cartItemEntity.setQuantity(newQuantity);
                            holder.textViewQuantity.setText(String.valueOf(newQuantity));
                            holder.textViewProductPrice.setText(String.valueOf(newQuantity * cartItemEntity.getProduct().getProductPrice()));
                            total = total - cartItemEntity.getProduct().getProductPrice();
                            textViewTempPrice.setText(String.valueOf(total));
                            textViewTotalPrice.setText(String.valueOf(total));
                        }
                    }
                });
                holder.imageViewAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemCartListener.onClickSubtract(cartItemEntity);
                        int newQuantity = cartItemEntity.getQuantity() + 1;

                        if (newQuantity <= cartItemEntity.getProduct().getProductQuantity()) {
                            cartItemEntity.setQuantity(newQuantity);
                            holder.textViewQuantity.setText(String.valueOf(newQuantity));
                            holder.textViewProductPrice.setText(String.valueOf(newQuantity * cartItemEntity.getProduct().getProductPrice()));
                            total = total + cartItemEntity.getProduct().getProductPrice();
                            textViewTempPrice.setText(String.valueOf(total));
                            textViewTotalPrice.setText(String.valueOf(total));
                        }
                    }
                });
            }
        }
    }
    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView
                textViewSize, textViewQuantity, textViewProductPrice;
        private ImageView imageViewDelete, imageViewSub, imageViewAdd;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSize = itemView.findViewById(R.id.cart_size);
            textViewQuantity = itemView.findViewById(R.id.cart_quantity);
            textViewProductPrice = itemView.findViewById(R.id.product_price_cart);
            imageViewDelete = itemView.findViewById(R.id.imageView_delete_product);
            imageViewSub = itemView.findViewById(R.id.subtract_quantity);
            imageViewAdd = itemView.findViewById(R.id.add_quantity);
        }
    }
    public void footerTotal() {
        textViewTotalPrice.setText(String.valueOf(total));
        textViewTempPrice.setText(String.valueOf(total));
    }
}