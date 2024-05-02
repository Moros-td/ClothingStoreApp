package com.example.clothingstoreapp.adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.activity.AddressManagementActivity;
import com.example.clothingstoreapp.custom_interface.IClickItemAddress;
import com.example.clothingstoreapp.custom_interface.IClickItemEditAddressListener;
import com.example.clothingstoreapp.entity.AddressEntity;

import java.util.List;

public class AddressManagementAdapter extends RecyclerView.Adapter<AddressManagementAdapter.AddressManagementViewHolder> implements Filterable {
    private List<AddressEntity> list;
    private TextView textViewTotalPrice, textViewTempPrice;
    private IClickItemEditAddressListener iClickItemEditAddressListener;
    private AddressManagementActivity activity; // Thêm biến tham chiếu đến Activity

    public AddressManagementAdapter(List<AddressEntity> list, IClickItemEditAddressListener listener) {
        this.list = list;
        this.iClickItemEditAddressListener = listener;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public AddressManagementAdapter.AddressManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_management, parent, false);
        return new AddressManagementAdapter.AddressManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressManagementAdapter.AddressManagementViewHolder holder, int position) {
        AddressEntity addressEntity = list.get(position);

        if (addressEntity == null) {
            return;
        } else {
            holder.textViewName.setText(addressEntity.getName());
            holder.textViewPhone.setText(addressEntity.getPhone());
            holder.textViewAddress.setText(addressEntity.getAddress());
            if(addressEntity.getIs_default() == 1){
                holder.addressDefault.setVisibility(View.VISIBLE);
            }else{
                holder.addressDefault.setVisibility(View.GONE);
            }
            // Xử lý sự kiện khi ImageView được bấm
            holder.imageViewOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Tạo PopupMenu
                    PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.imageViewOption);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_address_option, popupMenu.getMenu());

                    // Xử lý sự kiện khi một mục trong PopupMenu được chọn
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menu_set_default) {
                                iClickItemEditAddressListener.onClickSetDefaultAddress(addressEntity);
                                return true;
                            }
                            if (item.getItemId() == R.id.menu_edit) {
                                iClickItemEditAddressListener.onClickEditAddress(addressEntity);
                                return true;
                            }
                            if (item.getItemId() == R.id.menu_delete) {
                                iClickItemEditAddressListener.onClickDeleteAddress(addressEntity);
                               return true;
                            }
                            return false;

                        }
                    });

                    // Hiển thị PopupMenu
                    popupMenu.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AddressManagementViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName, textViewPhone, textViewAddress;
        private LinearLayout addressDefault;
        private ImageView imageViewOption;

        public AddressManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView_name_user_mg);
            textViewPhone = itemView.findViewById(R.id.textView_phoneNumber_mg);
            textViewAddress = itemView.findViewById(R.id.textView_address_mg);
            addressDefault = itemView.findViewById(R.id.address_default);
            imageViewOption = itemView.findViewById(R.id.im_option_address);
        }
    }
}