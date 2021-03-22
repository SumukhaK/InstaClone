package com.ksa.instagramclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksa.instagramclone.R;
import com.ksa.instagramclone.activities.HomeActivity;
import com.ksa.instagramclone.models.Commentmodel;
import com.ksa.instagramclone.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    Context context;
    ArrayList<Commentmodel> commentmodels;
    FirebaseUser firebaseUser;

    public CommentsAdapter(Context context, ArrayList<Commentmodel> commentmodels) {
        this.context = context;
        this.commentmodels = commentmodels;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View commentsView = LayoutInflater.from(context).inflate(R.layout.item_comments,parent,false);

        return new ViewHolder(commentsView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Commentmodel commentmodel = commentmodels.get(position);

        holder.commentTv.setText(commentmodel.getComment());
        FirebaseDatabase.getInstance().getReference().child("Users").child(commentmodel.getPublisher()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Log.v("Comments ",userModel.toString());
                        holder.userNameTv.setText(userModel.getUserName());
                        if(userModel.getImage_url().equals("default")){
                           holder.profileImageview.setImageResource(R.mipmap.ic_launcher_round);
                        }else {
                            Picasso.get().load(userModel.getImage_url()).into(holder.profileImageview);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        holder.profileImageview.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.putExtra("publisherId",commentmodel.getPublisher());
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return commentmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView userNameTv;
        public TextView commentTv;
        public CircleImageView profileImageview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTv = itemView.findViewById(R.id.username);
            commentTv = itemView.findViewById(R.id.comment_tv);
            profileImageview = itemView.findViewById(R.id.profileimage);

        }
    }
}
