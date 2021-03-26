package com.ksa.instagramclone.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksa.instagramclone.R;
import com.ksa.instagramclone.fragments.PostDetails;
import com.ksa.instagramclone.models.NotificationModel;
import com.ksa.instagramclone.models.PostModel;
import com.ksa.instagramclone.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    Context context;
    ArrayList<NotificationModel> notificationModels= new ArrayList<>();
    PostModel postModel;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationModels) {
        this.context = context;
        this.notificationModels = notificationModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View notificationView = LayoutInflater.from(context).inflate(R.layout.item_notification,parent,false);

        return new ViewHolder(notificationView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NotificationModel notificationModel = notificationModels.get(position);
        Log.v("NotificationAdapter"," "+notificationModel.toString());
        getuser(holder.userNameTv,holder.profileImageView,notificationModel);
        holder.contentTextview.setText(""+notificationModel.getText());
        if(notificationModel.getIsPost().equalsIgnoreCase("true")){
            holder.postImageview.setVisibility(View.VISIBLE);
            getPostImage(holder.postImageview,notificationModel.getPostId());
        }else{
            holder.postImageview.setVisibility(View.GONE);
        }

        holder.notificationItemLayout.setOnClickListener(v -> {
            if(notificationModel.getIsPost().equalsIgnoreCase("true")) {

                String postId = notificationModel.getPostId();
                Bundle bundle = new Bundle();
                bundle.putString(PostDetails.ARG_PARAM1, postId);
                Fragment fragment = new PostDetails();
                fragment.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();

            }
        });


    }

    private void getPostImage(ImageView postImageview, String postId) {

        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        postModel = snapshot.getValue(PostModel.class);
                        Log.v("NotificationPost",postModel.toString());
                        try{
                            Picasso.get().load(postModel.getImageURL()).into(postImageview);
                        }catch (Exception e){
                            e.printStackTrace();
                            postImageview.setImageResource(R.mipmap.ic_launcher);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getuser(TextView userNameTv, CircleImageView profileImageView, NotificationModel notificationModel) {

        FirebaseDatabase.getInstance().getReference().child("Users").child(notificationModel.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Map<String,String> map=(Map<String,String>)snapshot.getValue();
                        UserModel userModel = new UserModel(map.get("name"),map.get("email"),map.get("userName"),
                                map.get("bio"),map.get("image_url"),map.get("id"));
                        userNameTv.setText(userModel.getUserName());

                        try {
                            Picasso.get().load(userModel.getImage_url()).into(profileImageView);
                        }catch (Exception e){
                            e.printStackTrace();
                            profileImageView.setImageResource(R.mipmap.ic_launcher_round);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView profileImageView;
        public RelativeLayout notificationItemLayout;
        public TextView userNameTv;
        public TextView contentTextview;
        public ImageView postImageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImageView = itemView.findViewById(R.id.notification_profile);
            notificationItemLayout = itemView.findViewById(R.id.item_layout);
            userNameTv = itemView.findViewById(R.id.notification_username);
            contentTextview = itemView.findViewById(R.id.notification_text);
            postImageview = itemView.findViewById(R.id.post_image);
        }
    }
}
