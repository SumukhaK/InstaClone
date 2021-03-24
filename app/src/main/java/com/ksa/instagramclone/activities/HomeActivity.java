package com.ksa.instagramclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksa.instagramclone.R;
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
                    //getSharedPreferences("Profile",MODE_PRIVATE).edit().putString("publisherId","").apply();
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

        Bundle intent = getIntent().getExtras();
        if(intent != null){
            String profileId = intent.getString("publisherId");
            getSharedPreferences("Profile",MODE_PRIVATE).edit().putString("publisherId",profileId).apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }

    /*public void moveFirebaseRecord()
    {
        FirebaseDatabase.getInstance().getReference().child("follow").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                FirebaseDatabase.getInstance().getReference().child("Follow").setValue(dataSnapshot.getValue()).addOnCompleteListener(task -> {

                    Log.v("moveFirebaseRecord",   " "+task.isSuccessful());
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("moveFirebaseRecord",   "error "+error);
            }

        });
    }*/
}