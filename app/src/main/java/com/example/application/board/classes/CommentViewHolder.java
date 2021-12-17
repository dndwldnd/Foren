package com.example.application.board.classes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;


public class CommentViewHolder extends RecyclerView.ViewHolder {
    public TextView authorView;
    public TextView messageView;

    public CommentViewHolder(View itemView) {
        super(itemView);
        authorView = itemView.findViewById(R.id.commentAuthor);
        messageView = itemView.findViewById(R.id.commentMessage);
    }
}
