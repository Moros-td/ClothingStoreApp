package com.example.clothingstoreapp.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingstoreapp.R;
import com.example.clothingstoreapp.entity.CommentEntity;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    private List<CommentEntity> listComment;

    public CommentAdapter(List<CommentEntity> listComment) {
        this.listComment = listComment;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);

        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentEntity commentEntity = listComment.get(position);
        if(commentEntity == null){
            return;
        }
        holder.commentTextView.setText(commentEntity.getComment());
        holder.emailTextView.setText(commentEntity.getEmail());
        holder.ratingBar.setRating((float) commentEntity.getRating());
        holder.commentDateTextView.setText(commentEntity.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        if(listComment != null)
            return listComment.size();
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        private TextView emailTextView, commentTextView, commentDateTextView;

        private RatingBar ratingBar;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            emailTextView = itemView.findViewById(R.id.emailTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            commentDateTextView = itemView.findViewById(R.id.commentDateTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ffc922")));
        }

    }
}
