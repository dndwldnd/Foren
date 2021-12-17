package com.example.application.board;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.board.classes.Comment;
import com.example.application.board.classes.CommentViewHolder;
import com.example.application.board.classes.Post;
import com.example.application.board.classes.User;
import com.example.application.databinding.ActivityBoardViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BoardView extends AppCompatActivity {

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference postDatabaseReference;
    private DatabaseReference commentDatabaseReference;
    private ValueEventListener postEventListener;
    private String postKey;
    private CommentAdapter commentAdapter;

    private String boardName;

    private ActivityBoardViewBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_view);
        binding = ActivityBoardViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        boardName = getIntent().getStringExtra("board_name");

        if (postKey == null) {
            throw new IllegalArgumentException("EXTRA_POST_KEY ERROR");
        }

        postDatabaseReference = FirebaseDatabase.getInstance().getReference().child(boardName).child(postKey);
        commentDatabaseReference = FirebaseDatabase.getInstance().getReference().child("post-comments").child(postKey);

        binding.buttonPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uid = getUid();
                FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                assert user != null;
                                String authorName = user.username;
                                String commentMessage = binding.fieldCommentText.getText().toString();

                                if (commentMessage.equals("")) {
                                    Toast.makeText(BoardView.this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Comment comment = new Comment(uid, authorName, commentMessage);
                                    commentDatabaseReference.push().setValue(comment);
                                    binding.fieldCommentText.setText(null);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        binding.recyclerPostComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                binding.postAuthorLayout.postAuthor.setText(post.getAuthor());
                binding.postTextLayout.postTitle.setText(post.getTitle());
                binding.postTextLayout.postMessage.setText(post.getMessage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "글을 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        };

        postDatabaseReference.addValueEventListener(postListener);

        postEventListener = postListener;

        commentAdapter = new CommentAdapter(getApplicationContext(), commentDatabaseReference);
        binding.recyclerPostComments.setAdapter(commentAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (postEventListener != null) {
            postDatabaseReference.removeEventListener(postEventListener);
        }

        commentAdapter.cleanupListener();

    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> commentIds = new ArrayList<>();
        private List<Comment> comments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference databaseReference) {
            mContext = context;
            mDatabaseReference = databaseReference;

            ChildEventListener childEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Comment comment = snapshot.getValue(Comment.class);

                    commentIds.add(snapshot.getKey());
                    comments.add(comment);
                    notifyItemInserted(comments.size() - 1);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Comment newComment = snapshot.getValue(Comment.class);
                    String commentKey = snapshot.getKey();

                    int commentIndex = commentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        comments.set(commentIndex, newComment);

                        notifyItemChanged(commentIndex);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    String commentKey = snapshot.getKey();
                    int commentIndex = commentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        commentIds.remove(commentIndex);
                        comments.remove(commentIndex);

                        notifyItemRemoved(commentIndex);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Comment movedComment = snapshot.getValue(Comment.class);
                    String commentKey = snapshot.getKey();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(mContext, "댓글을 불러오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            };
            databaseReference.addChildEventListener(childEventListener);

            mChildEventListener = childEventListener;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            Comment comment = comments.get(position);
            holder.authorView.setText(comment.getAuthor());
            holder.messageView.setText(comment.getMessage());
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}