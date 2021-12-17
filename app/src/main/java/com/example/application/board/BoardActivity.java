package com.example.application.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.application.R;
import com.example.application.board.classes.Post;
import com.example.application.board.classes.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;



public class BoardActivity extends AppCompatActivity {

    private TextView textView;
    private FloatingActionButton floatingActionButton;

    private String boardName;
    private String boardNameKor;

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        textView = findViewById(R.id.board_activity_text);
        floatingActionButton = findViewById(R.id.fab_button);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.PostRecyclerView);
        recyclerView.setHasFixedSize(true);

        boardNameKor = getIntent().getStringExtra("text");

        textView.setText(boardNameKor);

        linearLayoutManager = new LinearLayoutManager(BoardActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        Query postsQuery = getQuery(databaseReference);

        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.item_post, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
                final DatabaseReference postRef = getRef(position);

                final String postKey = postRef.getKey();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BoardActivity.this, BoardView.class);
                        intent.putExtra(BoardView.EXTRA_POST_KEY, postKey);
                        intent.putExtra("board_name", boardName);
                        startActivity(intent);
                    }
                });

                holder.bindToPost(model);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoardActivity.this, AddPost.class);
                intent.putExtra("board_name", boardName);
                startActivity(intent);
            }
        });

    }


    public Query getQuery(DatabaseReference databaseReference) {
        Query query;
        switch (boardNameKor) {
            case "자유게시판":
                boardName = "General";
                query = databaseReference.child(boardName).limitToFirst(100);
                break;
            case "정보게시판":
                boardName = "Info";
                query = databaseReference.child(boardName).limitToFirst(100);
                break;
            case "질문게시판":
                boardName = "QnA";
                query = databaseReference.child(boardName).limitToFirst(100);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getIntent().getStringExtra("text"));
        }
        return query;
    }

}
