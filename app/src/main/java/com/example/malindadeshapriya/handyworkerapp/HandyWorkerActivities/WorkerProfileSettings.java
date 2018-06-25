package com.example.malindadeshapriya.handyworkerapp.HandyWorkerActivities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.Entity.HandyWorker;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


import java.net.URI;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkerProfileSettings extends Fragment {

    View v;

    ImageButton btnProfileImage;
    Button btnSubmit;

    EditText etName,etEmail,etPhone,etCat,etRating;
    private static int GALLERY_REQUEST = 1;

    private Uri imageUri = null;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference  storageReference;

    private String userId;


    public WorkerProfileSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_worker_profile_settings, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        btnProfileImage = (ImageButton)v.findViewById(R.id.ib_worker_ps_profileImage);
        btnSubmit = (Button)v.findViewById(R.id.btn_worker_ps_submit);
        etName = (EditText)v.findViewById(R.id.et_worker_ps_name);
        etEmail = (EditText)v.findViewById(R.id.et_worker_ps_email);
        etPhone = (EditText)v.findViewById(R.id.et_worker_ps_phone);
        etCat = (EditText)v.findViewById(R.id.et_worker_ps_cat);
        etRating = (EditText)v.findViewById(R.id.et_worker_ps_ratings);

        etPhone.setFocusable(false);
        etCat.setFocusable(false);
        etRating.setFocusable(false);

        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HandyWorkers");

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HandyWorker handyWorker =dataSnapshot.getValue(HandyWorker.class);

                String url = handyWorker.getImage();
                etName.setText(handyWorker.getName());
                etEmail.setText(handyWorker.getEmail());
                etPhone.setText(handyWorker.getPhone());
                etCat.setText(handyWorker.getCategory());
                etRating.setText(Double.toString(handyWorker.getRating()));


                if(!url.equals("null")){
                    Picasso.with(v.getContext()).load(url).resize(500,500).into(btnProfileImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*databaseReference.child(userId).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                if(!url.equals("null")){
                    Picasso.with(v.getContext()).load(url).resize(500,500).into(btnProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        btnProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST );

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });


        return v;
    }

    private void startSetupAccount() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        String name = etName.getText().toString().trim();

        final String user_id = firebaseAuth.getCurrentUser().getUid();

        if(imageUri != null ){

            StorageReference filepath = storageReference.child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String  downloadUri = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.child(user_id).child("image").setValue(downloadUri);
                        progressDialog.dismiss();


                }
            });



            // databaseReference.child(user_id).setValue("default");

        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST &&  resultCode == RESULT_OK){

            Uri imageuri = data.getData();

            CropImage.activity()
                    .start(getContext(), this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri= result.getUri();
                btnProfileImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }



    }
}
