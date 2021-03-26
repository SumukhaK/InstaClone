package com.ksa.instagramclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ksa.instagramclone.R;
import com.ksa.instagramclone.adapters.NotificationAdapter;
import com.ksa.instagramclone.models.NotificationModel;
import com.ksa.instagramclone.models.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    private RecyclerView notifications;
    private NotificationAdapter notificationAdapter;
    ArrayList<NotificationModel> notificationModels = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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

    /*
    private String userId;
    private String text;
    private String postId;
    private boolean isPost;
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View notiView = inflater.inflate(R.layout.fragment_notification, container, false);

        notifications = notiView.findViewById(R.id.notifications_list);
        notifications.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationAdapter = new NotificationAdapter(getContext(),notificationModels);
        notifications.setAdapter(notificationAdapter);

        getNotifications();

        return notiView;
    }

    private void getNotifications() {

        FirebaseDatabase.getInstance().getReference().child("Notifications").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                            Map<String,String> map=(Map<String,String>)dataSnapshot.getValue();
                            NotificationModel notificationModel = new NotificationModel(map.get("userId"),map.get("text"),map.get("postId"),
                                    map.get("isPost"));

                            Log.v("Notification"," "+notificationModel.toString());
                            notificationModels.add(notificationModel);
                        }
                        Collections.reverse(notificationModels);
                        notificationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
}