package com.example.application.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.board.classes.Post;
import com.example.application.board.classes.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPost extends AppCompatActivity {

    DatabaseReference databaseReference;
    FloatingActionButton floatingActionButton;
    EditText title;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        floatingActionButton = findViewById(R.id.fab_add_button);
        title = findViewById(R.id.addTitle);
        message = findViewById(R.id.addMessage);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textTitle = title.getText().toString();
                String textMessage = message.getText().toString();

                if (textTitle.isEmpty() || textMessage.isEmpty()) {
                    Toast.makeText(AddPost.this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                floatingActionButton.setEnabled(false);

                final String userId = getUid();
                databaseReference.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);

                                if (user == null) {
                                    Toast.makeText(AddPost.this, "유저를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    writeNewPost(userId, user.username, textTitle, textMessage, getIntent().getStringExtra("board_name"));
                                    finish();
                                }

                                floatingActionButton.setEnabled(true);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(AddPost.this, "유저를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                                floatingActionButton.setEnabled(true);
                            }
                        }
                );
            }
        });

    }


    private String getUid() { return FirebaseAuth.getInstance().getCurrentUser().getUid(); }

    private void writeNewPost(String userId, String username, String title, String message, String board) {

        String key = databaseReference.child(board).push().getKey();
        Post post = new Post(userId, title, message, username);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + board + "/" + key, postValues);

        databaseReference.updateChildren(childUpdates);
    }
}