package com.ksa.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ImageView iconImage;
    private LinearLayout linearLayout;
    private Button registerBtn,loginBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconImage = findViewById(R.id.icon_iv);
        linearLayout = findViewById(R.id.linear_layout);
        registerBtn = findViewById(R.id.register_btn);
        loginBtn = findViewById(R.id.login_btn);

        mAuth = FirebaseAuth.getInstance();

        linearLayout.animate().alpha(0f).setDuration(1);

        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,-1000);
        translateAnimation.setDuration(1500);
        translateAnimation.setFillAfter(false);
        translateAnimation.setAnimationListener(new MyAnimationListner());
        iconImage.setAnimation(translateAnimation);


        registerBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        loginBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)));
    }

    private class MyAnimationListner implements Animation.AnimationListener{


        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            iconImage.clearAnimation();
            iconImage.setVisibility(View.INVISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentUser=mAuth.getCurrentUser();
        //Log.v("FireBaseInsta" ,"mCurrentUser  "+mCurrentUser.toString());
        if(mCurrentUser != null) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Log.v("FireBaseInsta" ,"mCurrentUser  "+FirebaseAuth.getInstance().getCurrentUser().toString() );
                startActivity(new Intent(MainActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finishAffinity();
            }
        }else{
            Log.v("FireBaseInsta" ,"mCurrentUser is null.. ");
        }
    }
}