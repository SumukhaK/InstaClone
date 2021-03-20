package com.ksa.instagramclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksa.instagramclone.R;
import com.ksa.instagramclone.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    ArrayList<UserModel> userModels;
    boolean isFragment;
    private FirebaseUser firebaseUser;

    public UserAdapter(Context context, ArrayList<UserModel> userModels, boolean isFragment) {
        this.context = context;
        this.userModels = userModels;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_users,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserModel user = userModels.get(position);
        holder.followBtn.setVisibility(View.VISIBLE);

        holder.userNameTv.setText(user.getUserName());
        holder.fullNameTv.setText(user.getName());

        Picasso.get().load(user.getImage_url()).placeholder(R.mipmap.ic_launcher_round).into(holder.profileImageView);

        isFollowed(user.get_id(),holder.followBtn);

        if(user.get_id().equals(firebaseUser.getUid())){
            holder.followBtn.setVisibility(View.GONE);
        }

        holder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.followBtn.getText().toString().equals("Follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.get_id()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("follow")
                            .child(user.get_id()).child("followers").child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.get_id()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("follow")
                            .child(user.get_id()).child("followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });
    }

    private void isFollowed(String id, Button followBtn) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(id).exists()){
                    followBtn.setText("Following");
                }else{
                    followBtn.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView profileImageView;
        public TextView userNameTv;
        public TextView fullNameTv;
        public Button followBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImageView = itemView.findViewById(R.id.profile_image);
            userNameTv = itemView.findViewById(R.id.username_textview);
            fullNameTv = itemView.findViewById(R.id.fullname_textview);
            followBtn = itemView.findViewById(R.id.follow_button);
        }
    }

}
