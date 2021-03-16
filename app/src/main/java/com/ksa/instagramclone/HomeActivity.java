package com.ksa.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ksa.instagramclone.fragments.HomeFragment;
import com.ksa.instagramclone.fragments.NotificationFragment;
import com.ksa.instagramclone.fragments.ProfileFragment;
import com.ksa.instagramclone.fragments.SearchFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.nav_home:
                    selectorFragment = new HomeFragment();
                    break;

                case R.id.nav_add:
                    selectorFragment = null;
                    startActivity(new Intent(HomeActivity.this,PostActivity.class));
                    break;

                case R.id.nav_search:
                    selectorFragment = new SearchFragment();
                    break;

                case R.id.nav_profile:
                    selectorFragment = new ProfileFragment();
                    break;

                case R.id.nav_heart:
                    selectorFragment = new NotificationFragment();
                    break;
            }

            if(selectorFragment != null){

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectorFragment).commit();

            }
            return  true;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }
}