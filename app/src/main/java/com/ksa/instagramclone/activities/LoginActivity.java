package com.ksa.instagramclone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ksa.instagramclone.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEt,passwordEt;
    private Button loginBtn;
    //private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;
    private TextView registerUserTv;

    private String userNameSt,passwordSt,emailSt,fullNameSt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        registerUserTv = findViewById(R.id.reg_user_tv);
        loginBtn = findViewById(R.id.login_btn);

        mAuth = FirebaseAuth.getInstance();

        registerUserTv.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logInUser();
            }
        });

    }

    private void logInUser() {

        emailSt = emailEt.getText().toString();
        passwordSt = passwordEt.getText().toString();

        if(TextUtils.isEmpty(emailSt) || TextUtils.isEmpty(passwordSt)){
            Toast.makeText(LoginActivity.this,"Please enter valid email and password !!..",Toast.LENGTH_LONG).show();
        }else{
            mAuth.signInWithEmailAndPassword(emailSt,passwordSt).addOnSuccessListener(authResult -> {

                Toast.makeText(LoginActivity.this,"Logged in Successfully..",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            }).addOnFailureListener(e -> {

                Toast.makeText(LoginActivity.this,"Log in failed : "+e.getMessage(),Toast.LENGTH_LONG).show();
            });
        }

    }
}