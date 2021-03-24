package com.ksa.instagramclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksa.instagramclone.R;
import com.ksa.instagramclone.adapters.CommentsAdapter;
import com.ksa.instagramclone.models.Commentmodel;
import com.ksa.instagramclone.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity {


    private EditText addCommentsEt;
    private CircleImageView profileImageView;
    private RecyclerView commentsList;
    private TextView postTv;

    private String postId,authId;
    private FirebaseUser currentUser;

    private CommentsAdapter commentsAdapter;
    private ArrayList<Commentmodel> commentmodels= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = findViewById(R.id.comments_toolbar);
        addCommentsEt = findViewById(R.id.comment_et);
        profileImageView = findViewById(R.id.image_profile);
        postTv = findViewById(R.id.post);
        commentsList = findViewById(R.id.comments_recyclerview);
        commentsList.setHasFixedSize(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        authId = intent.getStringExtra("authId");
        //Log.v("Comments","postId "+postId+" authId "+authId);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        getUserImage();


        postTv.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(addCommentsEt.getText().toString())){
                addCommentToDb(addCommentsEt.getText().toString());
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentsActivity.this);
        /*linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);*/
        commentsList.setLayoutManager(linearLayoutManager);

        commentsAdapter = new CommentsAdapter(CommentsActivity.this,commentmodels);
        commentsList.setAdapter(commentsAdapter);

        getAllComments();
    }

    private void getAllComments() {

        FirebaseDatabase.getInstance().getReference().child("comments").child(postId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentmodels.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Log.v("CommentAll"," "+dataSnapshot.toString());
                            //Map<String,String> map=(Map<String,String>)dataSnapshot.getValue();
                            // Commentmodel commentmodel  = new Commentmodel(map.get("id"),map.get("comment"),map.get("publisher"));
                           Commentmodel commentmodel = dataSnapshot.getValue(Commentmodel.class);
                            Log.v("CommentModel"," "+commentmodel.toString());
                            commentmodels.add(commentmodel);
                        }
                        commentsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    private void addCommentToDb(String comment) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("comments").child(postId);
        String pushId =reference.push().getKey();
        //String pushId =String.valueOf(System.currentTimeMillis());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id",pushId);
        hashMap.put("comment",comment);
        hashMap.put("publisher",currentUser.getUid());

        Log.v("CommentsMap "," "+hashMap.toString());
        //reference.child(pushId).push().setValue(hashMap).addOnCompleteListener(task -> {
        reference.push().setValue(hashMap).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Toast.makeText(CommentsActivity.this,"Comment added successfully !!..",Toast.LENGTH_LONG).show();
                addCommentsEt.setText("");
                /*finish();*/
            }else {
                Toast.makeText(CommentsActivity.this,"Couldn't add comment, please try again later !!..",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getUserImage() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.v("Comments",snapshot.toString());
                Map<String,String> map=(Map<String,String>)snapshot.getValue();
                //Log.v("Comments map.get(\"id\")"," val : "+map.get("id"));
                UserModel userModel = new UserModel(map.get("name"),map.get("email"),map.get("userName"),
                        map.get("bio"),map.get("image_url"),map.get("id"));

                //Log.v("Comments",userModel.toString());
                if(userModel.getImage_url().equals("default")){
                    profileImageView.setImageResource(R.mipmap.ic_launcher_round);
                }else {
                    Picasso.get().load(userModel.getImage_url()).into(profileImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}