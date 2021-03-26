package com.ksa.instagramclone.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ksa.instagramclone.R;
import com.ksa.instagramclone.models.UserModel;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    MaterialEditText fullNameEt,userNameEt,bioEt;
    TextView changeProfileImageTv,saveTv;
    ImageView closeIv,profileIv;
    private FirebaseUser firebaseUser;
    private Uri imageUri;
    private String imageURL;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullNameEt = findViewById(R.id.fullname_et);
        userNameEt = findViewById(R.id.username_et);
        bioEt= findViewById(R.id.bio_et);
        changeProfileImageTv = findViewById(R.id.changepic_tv);
        saveTv = findViewById(R.id.save_tv);
        closeIv = findViewById(R.id.close_iv);
        profileIv = findViewById(R.id.editprofile_iv);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("Uploads");


        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Map<String,String> map=(Map<String,String>)snapshot.getValue();
                        UserModel userModel = new UserModel(map.get("name"),map.get("email"),map.get("userName"),
                                map.get("bio"),map.get("image_url"),map.get("id"));
                        Log.v("EditProfile",userModel.toString());

                        fullNameEt.setText(userModel.getName());
                        userNameEt.setText(userModel.getUserName());
                        bioEt.setText(userModel.getBio());
                        imageURL = userModel.getImage_url();
                        if(userModel.getImage_url().equals("default")){
                            profileIv.setImageResource(R.mipmap.ic_launcher_round);
                        }else {
                            Picasso.get().load(userModel.getImage_url()).into(profileIv);
                        }                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        closeIv.setOnClickListener(v -> finish());

        changeProfileImageTv.setOnClickListener(v -> {
            CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
        });

        profileIv.setOnClickListener(v -> CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this));

        saveTv.setOnClickListener(v -> updateProfile());

    }

    private void updateProfile() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Profile...");
        progressDialog.show();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("name",fullNameEt.getText().toString());
        hashMap.put("userName",userNameEt.getText().toString());
        hashMap.put("bio",bioEt.getText().toString());
        hashMap.put("image_url",imageURL);
        Log.v("UpdateProfile",hashMap.toString());
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).
                updateChildren(hashMap).addOnSuccessListener(aVoid -> {
            progressDialog.dismiss();
            Toast.makeText(EditProfileActivity.this,"Profile updated Successfully !!..",Toast.LENGTH_LONG).show();
            finish();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(EditProfileActivity.this,"Update failed ! Please try again later..",Toast.LENGTH_LONG).show();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);

            imageUri = activityResult.getUri();
            uploadImage();

        }else{
            Toast.makeText(EditProfileActivity.this,"Please try again..",Toast.LENGTH_LONG).show();

        }
    }

    private void uploadImage() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();

        if (imageUri != null) {
            //StorageReference storageReference = FirebaseStorage.getInstance().getReference("Posts").child(imageUri.getLastPathSegment());
            storageReference.putFile(imageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageURL = downloadUri.toString();

                    FirebaseDatabase.getInstance().getReference().
                            child("Users").child(firebaseUser.getUid()).child("image_url").setValue(imageURL);


                    progressDialog.dismiss();


                } else {
                    Toast.makeText(EditProfileActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(EditProfileActivity.this,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(EditProfileActivity.this,"No Image was selected !.. ",Toast.LENGTH_LONG).show();
        };


    }
}