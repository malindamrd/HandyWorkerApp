package com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities;


import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSettingsFragment extends Fragment {


    ImageView prof;
    private FirebaseAuth firebaseAuth;

    private EditText user_name,user_email,user_password,user_phone;
    private Button btn_submit;
    ProgressDialog progressDialog;

    public ProfileSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_profile_settings2, container, false);

        prof = (ImageView)v.findViewById(R.id.imageButton);
        user_name = (EditText)v.findViewById(R.id.et_name);
        user_email = (EditText)v.findViewById(R.id.et_email);
        user_password = (EditText)v.findViewById(R.id.et_password);
        user_phone = (EditText)v.findViewById(R.id.et_phone);
        btn_submit = (Button)v.findViewById(R.id.btnUpdate);

        progressDialog = new ProgressDialog(v.getContext());

        firebaseAuth = FirebaseAuth.getInstance();

        String user = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference householdOwnersRef = FirebaseDatabase.getInstance().getReference("HouseholdOwners");
        final DatabaseReference userref = householdOwnersRef.child(user);
        DatabaseReference imageref = userref.child("image");
        DatabaseReference nameref = userref.child("name");
        DatabaseReference emailref = userref.child("email");
        DatabaseReference phoneref = userref.child("phone");


        imageref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);

                if(!url.equals("")){
                    //Picasso.with(v.getContext()).load(url).resize(500,500).into(prof);
                    Picasso.with(v.getContext()).load(url)
                            .resize(500, 500)
                            .into(prof, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) prof.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    prof.setImageDrawable(imageDrawable);
                                }
                                @Override
                                public void onError() {
                                    //prof.setImageResource(R.drawable.default_image);
                                }
                            });
                }
                //Toast.makeText(v.getContext(),url,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        nameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                user_name.setText(name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        emailref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue(String.class);
                user_email.setText(email);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        phoneref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue(String.class) != null){

                    String phone = dataSnapshot.getValue(String.class);
                    user_phone.setText(phone);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating Details..!!!");
                progressDialog.show();

                String name = user_name.getText().toString().trim();
                String email = user_email.getText().toString().trim();
                String phone = user_phone.getText().toString().trim();

                userref.child("name").setValue(name);
                userref.child("email").setValue(email);
                userref.child("phone").setValue(phone);

                progressDialog.dismiss();
                Toast.makeText(v.getContext(),"Details Updated..!!!",Toast.LENGTH_LONG).show();
                ProfileSettingsFragment profileSettingsFragment = new ProfileSettingsFragment();
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.relativelayout_for_fragment,profileSettingsFragment);
                fragmentTransaction.commit();

            }
        });


        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




}

