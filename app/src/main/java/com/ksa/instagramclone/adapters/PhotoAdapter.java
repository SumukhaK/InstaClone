package com.ksa.instagramclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksa.instagramclone.R;
import com.ksa.instagramclone.models.PostModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{

    Context context;
    ArrayList<PostModel> postModels = new ArrayList<>();


    public PhotoAdapter(Context context, ArrayList<PostModel> postModels) {
        this.context = context;
        this.postModels = postModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_photos,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PostModel postModel = postModels.get(position);
        Picasso.get().load(postModel.getImageURL()).into(holder.photoImage);
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView photoImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            photoImage = itemView.findViewById(R.id.image_post);
            //numOfPostsTv = itemView.findViewById(R.id.noofpost_tv);

        }
    }
}
