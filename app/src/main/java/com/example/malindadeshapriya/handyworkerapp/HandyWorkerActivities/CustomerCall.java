package com.example.malindadeshapriya.handyworkerapp.HandyWorkerActivities;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerCall extends Fragment {

    private Button btnAccept, btnReject;
    private TextView txtDistance,txtOwner;

    private double distance, workLati,workLongi;
    private String ownerID,status,workerID;
    private String ownername = "Test";
    private String requestKey;
    private double reqLati,reqLongi;


    //WorkerRequest workerRequest = null;

    DatabaseReference householdOwnersRef = FirebaseDatabase.getInstance().getReference("HouseholdOwners");
    DatabaseReference databaseReference;
    DatabaseReference workerRequest = FirebaseDatabase.getInstance().getReference("requestWorker");

    FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;
    Bundle bundle;
    String user;

    public CustomerCall() {
        // Required empty public constructor


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_customer_call, container, false);

        //disable back button
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK ) {

                    // leave this blank in order to disable the back press

                    return true;
                } else {
                    return false;
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();

        bundle = getArguments();
        distance = bundle.getDouble("distance");
        ownerID = bundle.getString("owner");
        requestKey = bundle.getString("requestKey");
        reqLati = bundle.getDouble("lati");
        reqLongi = bundle.getDouble("longi");
        status = bundle.getString("status");
        workerID = bundle.getString("worker");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Initialize ringtone and play when open the fragment
        final MediaPlayer ringtone = MediaPlayer.create(v.getContext(),R.raw.ringtone);
        ringtone.start();

        btnAccept = (Button)v.findViewById(R.id.btn_accept);
        btnReject = (Button)v.findViewById(R.id.btn_reject);
        txtDistance = (TextView)v.findViewById(R.id.txt_distanceCC);
        txtOwner = (TextView)v.findViewById(R.id.txt_ownerCC);


        //txtOwner.setText(ownername);
        String dis = Double.toString(distance);
        txtDistance.setText(dis+" KM Away");




         DatabaseReference db2 = householdOwnersRef.child(ownerID);
         db2.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 ownername = dataSnapshot.child("name").getValue().toString();
                 txtOwner.setText(dataSnapshot.child("name").getValue().toString());
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone.stop();
                DatabaseReference workRef = workerRequest.child(requestKey);
                workRef.child("status").setValue("Accepted");

                DatabaseReference handyWorkerRef = FirebaseDatabase.getInstance().getReference("HandyWorkers").child(user);

                handyWorkerRef.child("status").setValue("not available");

                Bundle bundle = new Bundle();
                bundle.putString("requestKey",requestKey);
                bundle.putString("name",ownername);

                RequestDetails requestDetails = new RequestDetails();
                requestDetails.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rel_frag_worker, requestDetails,"findThisFragment")
                        .addToBackStack(null)
                        .commit();







            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone.stop();

                DatabaseReference workRef = workerRequest.child(requestKey);
                workRef.child("status").setValue("Rejected");

                WorkerMapFragment workerMapFragment = new WorkerMapFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rel_frag_worker, workerMapFragment,"findThisFragment")
                        .addToBackStack(null)
                        .commit();


            }
        });



        return v;
    }

}
