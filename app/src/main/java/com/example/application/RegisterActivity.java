package com.example.application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.board.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        register = findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();

                if (textEmail.isEmpty() || textPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                }
                else if (textPassword.length() < 7) {
                    Toast.makeText(RegisterActivity.this, "패스워드를 7자 이상 적어주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(textEmail, textPassword);
                }
            }
        });
    }

    private void registerUser(String _email, String _password) {
        auth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = task.getResult().getUser();
                    String username = usernameFromEmail(user.getEmail());
                    writeNewUser(user.getUid(), username, user.getEmail());

                    Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private String getUid() { return FirebaseAuth.getInstance().getCurrentUser().getUid(); }

    private void writeNewUser(String userId, String name, String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(userId).child("username").setValue(name);
    }
}
