package com.ksa.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameEt,nameEt,emailEt,passwordEt;
    private Button registerBtn;
    private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;
    private TextView loginUserTv;

    private String userNameSt,passwordSt,emailSt,fullNameSt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameEt = findViewById(R.id.usernameEt);
        nameEt = findViewById(R.id.fullnameEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginUserTv = findViewById(R.id.already_reg_tv);
        registerBtn = findViewById(R.id.registerpage_regbtn);

        mDataRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        loginUserTv.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        registerBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        if(TextUtils.isEmpty(userNameEt.getText().toString()) || nameEt.getText().toString().isEmpty() || passwordEt.getText().toString().isEmpty()
                || emailEt.getText().toString().isEmpty()){

            Toast.makeText(RegisterActivity.this,"Fields cannot be empty !!...",Toast.LENGTH_LONG).show();
        }else if(emailEt.getText().toString().length()<=6) {
            Toast.makeText(RegisterActivity.this,"Password should be at least 6 character long",Toast.LENGTH_LONG).show();
        }else
        {
            userNameSt = userNameEt.getText().toString();
            passwordSt = passwordEt.getText().toString();
            emailSt = emailEt.getText().toString();
            fullNameSt = nameEt.getText().toString();

            mAuth.createUserWithEmailAndPassword(emailSt,passwordSt).addOnSuccessListener(authResult -> {

                HashMap<String,Object> map = new HashMap<>();
                map.put("email",authResult.getUser().getEmail());
                map.put("userName",authResult.getUser().getDisplayName());
                map.put("id",authResult.getUser().getUid());
                map.put("bio","");
                map.put("image_url","default");

                mDataRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(RegisterActivity.this,"Update the profile..",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finishAffinity();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this," Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                });

            });
        }

    }

}