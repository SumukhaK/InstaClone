package com.ksa.instagramclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksa.instagramclone.R;

import java.util.ArrayList;


public class HastagsAdapter extends RecyclerView.Adapter<HastagsAdapter.ViewHolder>{

    Context context;
    //ArrayList<HashTagModel> hashTagModels;
    ArrayList<String> hashTags;
    ArrayList<String> hashTagCount;

    public HastagsAdapter(Context context, ArrayList<String> hashTags, ArrayList<String> hashTagCount) {
        this.context = context;
        this.hashTags = hashTags;
        this.hashTagCount = hashTagCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tag_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.hashTagTv.setText(" #"+hashTags.get(position));
        holder.numOfPostsTv.setText(hashTagCount.get(position)+" posts");

    }

    @Override
    public int getItemCount() {
        return hashTags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView hashTagTv;
        public TextView numOfPostsTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            hashTagTv = itemView.findViewById(R.id.hashtag_tv);
            numOfPostsTv = itemView.findViewById(R.id.noofpost_tv);

        }
    }

    public void filter(ArrayList<String> mFilterTags,ArrayList<String> mTagsFilterCount){
        this.hashTags = mFilterTags;
        this.hashTagCount = mTagsFilterCount;

        notifyDataSetChanged();
    }
}
