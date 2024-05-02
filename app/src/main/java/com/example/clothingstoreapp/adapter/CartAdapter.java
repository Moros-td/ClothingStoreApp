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

import com.bumptech.glide.Glide;
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
            holder.textViewProductName.setText(cartItemEntity.getProduct().getProductName());
            holder.textViewSize.setText(cartItemEntity.getSize());
            holder.textViewQuantity.setText(String.valueOf(cartItemEntity.getQuantity()));
            String priceString = String.format(Locale.getDefault(), "%.3fđ", cartItemEntity.getTotalPrice());
            holder.textViewProductPrice.setText(priceString);
//set ảnh
            String path = cartItemEntity.getProduct().getImages().get(0);
            String pathImage = "";
            if (path != null && path.length() > 1) {
                String newPath = path.substring(1);
                pathImage = "http://10.0.2.2:8096"+newPath;
            }
            Glide.with(holder.imageViewProduct).load(pathImage).into(holder.imageViewProduct);

            // set màu
            if(cartItemEntity.getProduct().getProductColor().equals("red")){
                holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_red);
            }
            else if(cartItemEntity.getProduct().getProductColor().equals("pink")){
                holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_pink);
            }
            else if(cartItemEntity.getProduct().getProductColor().equals("yellow")){
                holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_yellow);
            }
            else if(cartItemEntity.getProduct().getProductColor().equals("green")){
                holder.viewProductColor.setBackgroundResource(R.drawable.circle_backgound_green);
            }
            else if(cartItemEntity.getProduct().getProductColor().equals("blue")){
                holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_blue);
            }
            else if(cartItemEntity.getProduct().getProductColor().equals("beige")){
                holder.viewProductColor.setBackgroundResource(R.drawable.cirlce_background_beige);
            }
            else if(cartItemEntity.getProduct().getProductColor().equals("white")){
                holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_white);
            }
            else if(cartItemEntity.getProduct().getProductColor().equals("black")){
                holder.viewProductColor.setBackgroundResource(R.drawable.circle_backgound_black);
            }
            else if(cartItemEntity.getProduct().getProductColor().equals("brown")){
                holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_brown);
            }
            else if(cartItemEntity.getProduct().getProductColor().equals("gray")){
                holder.viewProductColor.setBackgroundResource(R.drawable.circle_background_gray);
            }
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
                            String priceString = String.format(Locale.getDefault(), "%.3fđ", total);
                            textViewTempPrice.setText(priceString);
                            textViewTotalPrice.setText(priceString);
                        }
                    }
                });
                holder.imageViewAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemCartListener.onClickAdd(cartItemEntity);
                        int newQuantity = cartItemEntity.getQuantity() + 1;

                        if (newQuantity <= cartItemEntity.getProduct().getProductQuantity()) {
                            cartItemEntity.setQuantity(newQuantity);
                            holder.textViewQuantity.setText(String.valueOf(newQuantity));
                            holder.textViewProductPrice.setText(String.valueOf(newQuantity * cartItemEntity.getProduct().getProductPrice()));
                            total = total + cartItemEntity.getProduct().getProductPrice();
                            String priceString = String.format(Locale.getDefault(), "%.3fđ", total);
                            textViewTempPrice.setText(priceString);
                            textViewTotalPrice.setText(priceString);
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
                textViewSize, textViewQuantity, textViewProductPrice, textViewProductName;
        private ImageView imageViewDelete, imageViewSub, imageViewAdd, imageViewProduct;
        private View viewProductColor;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSize = itemView.findViewById(R.id.cart_size);
            textViewQuantity = itemView.findViewById(R.id.cart_quantity);
            textViewProductPrice = itemView.findViewById(R.id.product_price_cart);
            imageViewDelete = itemView.findViewById(R.id.imageView_delete_product);
            imageViewSub = itemView.findViewById(R.id.subtract_quantity);
            imageViewAdd = itemView.findViewById(R.id.add_quantity);
            imageViewProduct = itemView.findViewById(R.id.imageView_product);
            viewProductColor = itemView.findViewById(R.id.view_product_color);
            textViewProductName = itemView.findViewById(R.id.textView_product_name);
        }
    }
    public void footerTotal() {
        String priceString = String.format(Locale.getDefault(), "%.3fđ", total);
        textViewTempPrice.setText(priceString);
        textViewTotalPrice.setText(priceString);
    }
}