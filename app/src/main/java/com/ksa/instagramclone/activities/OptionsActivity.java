package com.ksa.instagramclone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ksa.instagramclone.R;

public class OptionsActivity extends AppCompatActivity {

    TextView settingsTv,logoutTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        settingsTv = findViewById(R.id.settings_tv);
        logoutTv = findViewById(R.id.logout_tv);

        logoutTv.setOnClickListener(v ->{
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
        );
    }
}