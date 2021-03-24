package com.ksa.instagramclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksa.instagramclone.R;
import com.ksa.instagramclone.adapters.PhotoAdapter;
import com.ksa.instagramclone.adapters.PostAdapter;
import com.ksa.instagramclone.models.PostModel;
import com.ksa.instagramclone.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private CircleImageView profileImageview;
    private TextView userNameTv,fullNameTv,bioTv,numOfPostsTv,numOfFollowTv,numOfFollowersTv;
    private Button editProfileBtn;
    private ImageView optionsIv,myPicsIv,savedPicsIv;
    private RecyclerView picsList,savedPicsList;
    private FirebaseUser firebaseUser;
    private PhotoAdapter photoAdapter;
    private PhotoAdapter savedPhotoAdapter;
    //private PostAdapter postAdapter;
    private ArrayList<PostModel> postModels;
    private ArrayList<PostModel> postSavedModels;
    private String profileId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getContext().getSharedPreferences("Profile",MODE_PRIVATE).getString("publisherId","");
        Log.v("Profile toStr"," "+data.toString());
        if(data == null || data.isEmpty() || data.length()==0){
            Log.v("Profile"," "+profileId);
            profileId = firebaseUser.getUid();
        }else{
            Log.v("Profile else"," "+data);
            profileId = data;
        }
        //getContext().getSharedPreferences("Profile",MODE_PRIVATE).edit().putString("publisherId","").apply();
        Log.v("Profile"," "+profileId);
        //Log.v("ProfilePref"," "+getContext().getSharedPreferences("Profile",MODE_PRIVATE).getString("publisherId",""));
        profileImageview = profileView.findViewById(R.id.imageprofile);
        optionsIv = profileView.findViewById(R.id.options_imageview);
        numOfPostsTv = profileView.findViewById(R.id.postsnum_tv);
        bioTv = profileView.findViewById(R.id.bio_tv);
        userNameTv = profileView.findViewById(R.id.profile_username_tv);
        fullNameTv = profileView.findViewById(R.id.fullname_tv);
        numOfFollowTv = profileView.findViewById(R.id.followingnum_tv);
        numOfFollowersTv = profileView.findViewById(R.id.followersnum_tv);
        editProfileBtn = profileView.findViewById(R.id.editpro_button);
        myPicsIv= profileView.findViewById(R.id.mypictures);
        savedPicsIv= profileView.findViewById(R.id.savedpictures);
        picsList= profileView.findViewById(R.id.pics_recyclerview);
        savedPicsList= profileView.findViewById(R.id.savedpics_recyclerview);

        if(profileId.equals(firebaseUser.getUid())){
            editProfileBtn.setText(R.string.caps_edit_profile);
        }else{
            checkFollowingStatus();
        }


        picsList.setHasFixedSize(true);
        savedPicsList.setHasFixedSize(true);
        picsList.setLayoutManager(new GridLayoutManager(getContext(),3));
        savedPicsList.setLayoutManager(new GridLayoutManager(getContext(),2));
        postModels = new ArrayList<>();
        postSavedModels = new ArrayList<>();

        photoAdapter = new PhotoAdapter(getContext(),postModels);
        picsList.setAdapter(photoAdapter);

        //postAdapter = new PostAdapter(getContext(),postSavedModels);
        savedPhotoAdapter = new PhotoAdapter(getContext(),postSavedModels);
        savedPicsList.setAdapter(savedPhotoAdapter);

        userInfo();
        getFollowCounts();
        getPostsCount();
        getMyPhotos();
        getSavedPosts();

        editProfileBtn.setOnClickListener(v -> {

            String btnText = editProfileBtn.getText().toString();
            if(btnText.equals(R.string.caps_edit_profile)){
                editProfile();
            }else if(btnText.equals("Follow")){
                FirebaseDatabase.getInstance().getReference().child("Follow").
                        child(firebaseUser.getUid()).child("following").child(profileId).setValue(true);

                FirebaseDatabase.getInstance().getReference().child("Follow").
                        child(profileId).child("followers").child(firebaseUser.getUid()).setValue(true);
            }else{
                FirebaseDatabase.getInstance().getReference().child("Follow").
                        child(firebaseUser.getUid()).child("following").child(profileId).removeValue();

                FirebaseDatabase.getInstance().getReference().child("Follow").
                        child(profileId).child("followers").child(firebaseUser.getUid()).removeValue();

            }
        });

        savedPicsIv.setOnClickListener(v -> {
            picsList.setVisibility(View.GONE);
            savedPicsList.setVisibility(View.VISIBLE);

        });

        myPicsIv.setOnClickListener(v -> {
            picsList.setVisibility(View.VISIBLE);
            savedPicsList.setVisibility(View.GONE);
        });

        return profileView;
    }

    private void getSavedPosts() {

        ArrayList<String> savedPostsIds = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("saves").child(firebaseUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                            savedPostsIds.add(dataSnapshot.getKey());
                        }
                        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        postSavedModels.clear();

                                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                            PostModel postModel = dataSnapshot.getValue(PostModel.class);
                                            Log.v("Posts"," "+postModel.toString());
                                            for(String id:savedPostsIds){
                                                if(postModel.getPostId().equals(id)) {
                                                    postSavedModels.add(postModel);
                                                }

                                            }
                                        }
                                        savedPhotoAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                }
                        );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    private void getMyPhotos() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postModels.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Log.v("Posts"," "+dataSnapshot.toString());
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    Log.v("Posts"," "+postModel.toString());
                    if(postModel.getPublisher().equals(profileId)){
                        postModels.add(postModel);
                    }
                }

                Collections.reverse(postModels);
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void editProfile() {

    }

    private void checkFollowingStatus() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(profileId).exists()){
                    editProfileBtn.setText("Following");
                }else {
                    editProfileBtn.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPostsCount() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    if(postModel.getPublisher().equals(profileId)){
                        count++;
                    }
                }
                numOfPostsTv.setText(count+" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFollowCounts() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                .child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                numOfFollowersTv.setText(snapshot.getChildrenCount()+" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).
                child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                numOfFollowTv.setText(snapshot.getChildrenCount()+" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void userInfo() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String,String> map=(Map<String,String>)snapshot.getValue();
                UserModel userModel = new UserModel(map.get("name"),map.get("email"),map.get("userName"),
                        map.get("bio"),map.get("image_url"),map.get("id"));
                Log.v("Profile",userModel.toString());

                if(userModel.getImage_url().equals("default")){
                    profileImageview.setImageResource(R.mipmap.ic_launcher_round);
                }else {
                    Picasso.get().load(userModel.getImage_url()).into(profileImageview);
                }
                userNameTv.setText(userModel.getUserName());
                fullNameTv.setText(userModel.getName());
                bioTv.setText(userModel.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().getSharedPreferences("Profile",MODE_PRIVATE).edit().putString("publisherId","").apply();
        Log.v("ProfilePref"," "+getContext().getSharedPreferences("Profile",MODE_PRIVATE).getString("publisherId",""));

    }
}