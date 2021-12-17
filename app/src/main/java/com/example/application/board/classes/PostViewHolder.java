package com.example.application.board.classes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView messageView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.postTitle);
        authorView = itemView.findViewById(R.id.postAuthor);
        messageView = itemView.findViewById(R.id.postMessage);
    }

    public void bindToPost(Post post) {
        titleView.setText(post.getTitle());
        authorView.setText(post.getAuthor());
        messageView.setText(post.getMessage());
    }
}
