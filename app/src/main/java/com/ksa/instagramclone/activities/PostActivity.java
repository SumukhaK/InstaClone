package com.ksa.instagramclone.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hendraanggrian.appcompat.socialview.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.ksa.instagramclone.R;
import com.theartofdev.edmodo.cropper.CropImage;


import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private ImageView closeIv,imageAddedIv;
    private TextView postTv;
    private SocialAutoCompleteTextView descriptionStv;
    private Uri imageUri;
    private String imageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        closeIv = findViewById(R.id.close);
        imageAddedIv = findViewById(R.id.image_added);
        postTv = findViewById(R.id.post_tv);
        descriptionStv = findViewById(R.id.description);


        closeIv.setOnClickListener(v -> {
            startActivity(new Intent(PostActivity.this,HomeActivity.class));
            finishAffinity(); });


        CropImage.activity().start(PostActivity.this);


        postTv.setOnClickListener(v -> uploadFile());

    }
    /*
    com.google.android.gms.tasks.RuntimeExecutionException: com.google.firebase.storage.StorageException: Object does not exist at location.
     */
// https://stackoverflow.com/questions/56930237/object-does-not-exists-at-location-at-android-firebase-upload-file
    /*private void uploadImage() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();

        if(imageUri != null){

            //StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(imageUri.getLastPathSegment());

            StorageTask uploadTask = filePath.putFile(imageUri);
            uploadTask.continueWithTask((Continuation) task -> {
                if(!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                Uri downloadUri = task.getResult();
                imageURL = downloadUri.toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                String postId = reference.push().getKey();
                HashMap<String,Object> map = new HashMap<>();
                map.put("postId",postId);
                map.put("imageURL",imageURL);
                map.put("description",descriptionStv.getText().toString());
                map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference.child(postId).setValue(map);

                DatabaseReference hashtagRef = FirebaseDatabase.getInstance().getReference().child("Hashtags");
                List<String> list = descriptionStv.getHashtags();

                if(!list.isEmpty()){

                    for(String tag : list){
                        map.clear();
                        map.put("tag",tag.toLowerCase());
                        map.put("postId",postId);
                        hashtagRef.child(tag.toLowerCase()).setValue(map);
                    }
                    progressDialog.dismiss();
                    startActivity(new Intent(PostActivity.this,HomeActivity.class));
                    finishAffinity();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(PostActivity.this,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(PostActivity.this,"No Image was selected !.. ",Toast.LENGTH_LONG).show();
        }
    }*/


    private void uploadFile() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();

        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Posts").child(imageUri.getLastPathSegment());
            storageReference.putFile(imageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageURL = downloadUri.toString();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = reference.push().getKey();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postId", postId);
                    map.put("imageURL", imageURL);
                    map.put("description", descriptionStv.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    reference.child(postId).setValue(map);

                    DatabaseReference hashtagRef = FirebaseDatabase.getInstance().getReference().child("Hashtags");
                    List<String> list = descriptionStv.getHashtags();

                    if (!list.isEmpty()) {

                        for (String tag : list) {
                            map.clear();
                            map.put("tag", tag.toLowerCase());
                            map.put("postId", postId);
                            hashtagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                        }
                        progressDialog.dismiss();
                        startActivity(new Intent(PostActivity.this, HomeActivity.class));
                        finishAffinity();
                        Toast.makeText(PostActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PostActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(PostActivity.this,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(PostActivity.this,"No Image was selected !.. ",Toast.LENGTH_LONG).show();
        };


    }

    /*private String getFileExtension(Uri imageUri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageUri));

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);

            imageUri = activityResult.getUri();
            imageAddedIv.setImageURI(imageUri);

        }else{
            Toast.makeText(PostActivity.this,"Please try again..",Toast.LENGTH_LONG).show();

            startActivity(new Intent(PostActivity.this,HomeActivity.class));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Post ","onStart()");
        ArrayAdapter<Hashtag> hashtagArrayAdapter = new HashtagArrayAdapter<>(getApplicationContext());
        FirebaseDatabase.getInstance().getReference().child("Hashtags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Log.v("Post ",dataSnapshot.toString());
                    hashtagArrayAdapter.add(new Hashtag(snapshot.getKey(), (int) dataSnapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.v("PostHashtagAdapter ","setHashtagAdapter "+hashtagArrayAdapter.toString());
        descriptionStv.setHashtagAdapter(hashtagArrayAdapter);
    }
}