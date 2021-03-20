package com.ksa.instagramclone.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.ksa.instagramclone.R;
import com.ksa.instagramclone.models.PostModel;
import com.ksa.instagramclone.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    Context context;
    ArrayList<PostModel> postModelArrayList;
    FirebaseUser firebaseUser;


    public PostAdapter(Context context, ArrayList<PostModel> postModelArrayList) {
        this.context = context;
        this.postModelArrayList = postModelArrayList;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View postView = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PostModel postModel = postModelArrayList.get(position);
        if(postModel.getImageURL().equals("default")){

        }else {
            Picasso.get().load(postModel.getImageURL()).into(holder.postImage);
        }
        holder.descriptionTv.setText(postModel.getDescription());

        FirebaseDatabase.getInstance().getReference().child("Users").child(postModel.getPublisher()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Picasso.get().load(userModel.getImage_url()).into(holder.profileImageView);
                        holder.userNameTv.setText(userModel.getUserName());
                        holder.authorTv.setText(userModel.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        isLiked(postModel.getPostId(),holder.likeImage);
        numOfLikes(postModel.getPostId(),holder.numOfLikesTv);

        holder.likeImage.setOnClickListener(v -> {
            //Log.v("POST","likeImage.setOnClickListener "+holder.likeImage.getTag());
            if(holder.likeImage.getTag().equals("Like")) {
                FirebaseDatabase.getInstance().getReference().child("Likes").child(postModel.getPostId())
                        .child(firebaseUser.getUid()).setValue(true);
                //Log.v("POST","equals(\"like\")");
            }else{
                FirebaseDatabase.getInstance().getReference().child("Likes").child(postModel.getPostId())
                        .child(firebaseUser.getUid()).removeValue();
                //Log.v("POST","equals(\"like\") else");
            }
        });

    }

    private void isLiked(String postId, ImageView imageView) {
        //Log.v("POST","isLiked");
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.v("POST","isLiked "+snapshot.toString());
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("Liked");
                    Log.v("POST","exists() ");
                }else{
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("Like");
                    //Log.v("POST","exists() else");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void numOfLikes(String postID,TextView textView){

        FirebaseDatabase.getInstance().getReference().child("Likes").child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.getChildrenCount()+" likes");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return postModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView profileImageView;
        public ImageView postImage,likeImage,commentImage,saveImage,moreImage;
        public TextView userNameTv;
        public TextView numOfLikesTv,authorTv,numOfCommentsTv;
        public SocialTextView descriptionTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImageView = itemView.findViewById(R.id.profile_image);
            userNameTv = itemView.findViewById(R.id.username_textview);
            postImage = itemView.findViewById(R.id.post_image);
            likeImage = itemView.findViewById(R.id.like_iv);
            commentImage = itemView.findViewById(R.id.comment_iv);
            saveImage = itemView.findViewById(R.id.save_iv);
            moreImage = itemView.findViewById(R.id.more_imageview);

            numOfLikesTv = itemView.findViewById(R.id.likes_tv);
            authorTv = itemView.findViewById(R.id.author_tv);
            numOfCommentsTv = itemView.findViewById(R.id.numofcomments_tv);
            descriptionTv = itemView.findViewById(R.id.description_stv);
        }
    }
}
