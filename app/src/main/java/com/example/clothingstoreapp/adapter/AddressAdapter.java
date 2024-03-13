package com.example.clothingstoreapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.custom_interface.IClickItemAddress;
import com.example.clothingstoreapp.entity.AddressEntity;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> implements Filterable {
    private List<AddressEntity> list;
    private TextView textViewTotalPrice, textViewTempPrice;
    private IClickItemAddress iClickItemAddress;
    int selectedPosition = 0;

    public AddressAdapter(List<AddressEntity> list, IClickItemAddress iClickItemAddress) {
        this.list = list;
        this.iClickItemAddress = iClickItemAddress;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_address, parent, false);
        return new AddressAdapter.AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        AddressEntity addressEntity = list.get(position);

        if (addressEntity == null) {
            return;
        } else {
            holder.textViewName.setText(addressEntity.getName());
            holder.textViewPhone.setText(addressEntity.getPhone());
            holder.textViewAddress.setText(addressEntity.getAddress());
//            RadioButton radioButton = holder.itemView.findViewById(R.id.radioButtonAddress);
            holder.radioButton.setChecked(position == selectedPosition);

            // set listener on radio button
            holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    // check condition
                    if (b) {
                        // When checked
                        // update selected position
                        selectedPosition = holder.getAdapterPosition();
                        // Call listener
                        iClickItemAddress.onClickChoose(addressEntity);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName, textViewPhone, textViewAddress;
        private RadioButton radioButton;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView_name_user);
            textViewPhone = itemView.findViewById(R.id.textView_phoneNumber);
            textViewAddress = itemView.findViewById(R.id.textView_address);
            radioButton = itemView.findViewById(R.id.radioButtonAddress);
        }
    }
}
