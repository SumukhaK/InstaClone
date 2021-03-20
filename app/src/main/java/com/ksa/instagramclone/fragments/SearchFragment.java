package com.ksa.instagramclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.ksa.instagramclone.R;
import com.ksa.instagramclone.adapters.HastagsAdapter;
import com.ksa.instagramclone.adapters.UserAdapter;
import com.ksa.instagramclone.models.UserModel;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewTags;
    private SocialAutoCompleteTextView searchBarTv;
    private ArrayList<UserModel> userModels = new ArrayList<>();
    private ArrayList<String> mHashTags = new ArrayList<>();
    private ArrayList<String> mHashTagCount = new ArrayList<>();
    private UserAdapter userAdapter;
    private HastagsAdapter hastagsAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View searchView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = searchView.findViewById(R.id.recyclerview_users);
        recyclerViewTags = searchView.findViewById(R.id.recyclerview_tags);
        searchBarTv = searchView.findViewById(R.id.searchbar_textview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewTags.setHasFixedSize(true);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext()));

        userAdapter = new UserAdapter(getActivity(),userModels,true);
        recyclerView.setAdapter(userAdapter);

        hastagsAdapter = new HastagsAdapter(getContext(),mHashTags,mHashTagCount);
        recyclerViewTags.setAdapter(hastagsAdapter);

        addUsers();

        searchBarTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().isEmpty()){
                    Log.v("SearchBefore ",s.toString());
                    /*if(s.toString().charAt(0) == '#'){
                        Log.v("SearchCharAt ",s.toString());
                        searchTags(s.toString());
                    }else {*/
                    searchUser(s.toString());
                    //}
                }else{
                    addUsers();
                    //readTags();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.v("Search ",s.toString());
                filterTags(s.toString());
            }
        });

        readTags();

        return searchView;
    }

    private void readTags() {

        FirebaseDatabase.getInstance().getReference().child("Hashtags").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mHashTags.clear();
                        mHashTagCount.clear();

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                            mHashTags.add(dataSnapshot.getKey());
                            mHashTagCount.add(dataSnapshot.getChildrenCount()+" ");
                            hastagsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    private void addUsers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(TextUtils.isEmpty(searchBarTv.getText().toString())){
                    userModels.clear();

                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Map<String,String> map=(Map<String,String>)dataSnapshot.getValue();
                        //Log.v("SearchUser ",dataSnapshot.getValue().toString());
                        //UserModel userModel = dataSnapshot.getValue(UserModel.class); for some strange reason id is nu when we parse the whole model class like this !!..

                       /* Log.v("SearchUser ",map.get("bio"));
                        Log.v("SearchUser ",map.get("email"));
                        Log.v("SearchUser ",map.get("id"));
                        Log.v("SearchUser ",map.get("image_url"));
                        Log.v("SearchUser ",map.get("name"));
                        Log.v("SearchUser ",map.get("userName"));*/
                        UserModel userModel = new UserModel(map.get("name"),map.get("email"),map.get("userName"),
                                map.get("bio"),map.get("image_url"),map.get("id"));
                        userModels.add(userModel);
                        //Log.v("SearchUser ",userModel.toString());
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUser(String searchParam){

        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("userName").startAt(searchParam).endAt(searchParam+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userModels.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Map<String,String> map=(Map<String,String>)dataSnapshot.getValue();
                    UserModel userModel = new UserModel(map.get("name"),map.get("email"),map.get("userName"),
                            map.get("bio"),map.get("image_url"),map.get("id"));
                    userModels.add(userModel);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void searchTags(String searchParam){

        ArrayList<String> mTags = new ArrayList<>();
        ArrayList<String> mTagsCount = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference().child("Hashtags")
                .orderByChild("tag").startAt(searchParam).endAt(searchParam+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.v("Search ",snapshot.toString());
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Map<String,String> map=(Map<String,String>)dataSnapshot.getValue();
                    Log.v("Search ",map.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void filterTags(String searchParam){

        ArrayList<String> mTags = new ArrayList<>();
        ArrayList<String> mTagsCount = new ArrayList<>();

        for(String s: mHashTags){
            if(searchParam.toLowerCase().equals(s)){
                mTags.add(s);
                mTagsCount.add(mHashTagCount.get(mHashTags.indexOf(searchParam)));
            }
        }

        hastagsAdapter.filter(mTags,mTagsCount);
    }

}