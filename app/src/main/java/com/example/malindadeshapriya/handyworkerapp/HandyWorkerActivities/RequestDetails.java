package com.example.malindadeshapriya.handyworkerapp.HandyWorkerActivities;


import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.Control.DistanceCalculate;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.MapFragment;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetails extends Fragment{

    private EditText etName, etDistance,etEta;
    private Button btnGetDirections,btnRejectOffer;
    private RatingBar rbOwnerRating;

    private Bundle bundle;

    private String requestKey;

    FirebaseAuth firebaseAuth;
    private String user;
    DatabaseReference workerReqRef;
    DatabaseReference handyWorkerRef;

    String householdOwnerName;
    String jobCategory;
    Double eta;
    Double longi;
    Double lati;
    Double distance;
    //String status;

    String status;

    public RequestDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_request_details, container, false);

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

        /**
         * Get Bundle arguments
         */
        bundle = getArguments();
        requestKey = bundle.getString("requestKey");
        householdOwnerName = bundle.getString("name");

        Toast.makeText(v.getContext(),requestKey+" : "+householdOwnerName,Toast.LENGTH_LONG).show();

        /**
         * Initialize buttons, editText and rating bar
         */
        etName = (EditText) v.findViewById(R.id.et__rd_ownerName);
        etDistance = (EditText) v.findViewById(R.id.et_rd_distance);
        etEta = (EditText) v.findViewById(R.id.et_eta);
        btnGetDirections = (Button) v.findViewById(R.id.btn_directions);
        btnRejectOffer = (Button) v.findViewById(R.id.btn_rejectOffer);
        rbOwnerRating = (RatingBar) v.findViewById(R.id.rb_ownerRatings);

        /**
         * get current user id
         */
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();

        etName.setText(householdOwnerName);

        //Database reference to requestWorker Node
        workerReqRef = FirebaseDatabase.getInstance().getReference("requestWorker").child(requestKey);



        workerReqRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lati = Double.parseDouble(dataSnapshot.child("lati").getValue().toString());
                longi = Double.parseDouble(dataSnapshot.child("longi").getValue().toString());
                distance = Double.parseDouble(dataSnapshot.child("distance").getValue().toString());
                etDistance.setText(Double.toString(distance));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        rbOwnerRating.setRating((float) 4.6);

        btnGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("google.navigation:q="+lati+","+longi+""));
                Intent chooser = Intent.createChooser(intent,"Launch Map");
                startActivity(chooser);
            }
        });



        workerReqRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals("status")){
                    status = dataSnapshot.getValue().toString();

                    if(status!=null&&!status.isEmpty()&&!status.equals("null")&&status.equals("Completed")){
                        Toast.makeText(v.getContext(),"Completed",Toast.LENGTH_LONG).show();
                        WorkerMapFragment mapFragment = new WorkerMapFragment();
                        getActivity().getFragmentManager().popBackStack();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.rel_frag_worker, mapFragment,"findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }

                    if(status!=null&&!status.isEmpty()&&!status.equals("null")&&status.equals("Rejected")){
                        Toast.makeText(v.getContext(),"Rejected",Toast.LENGTH_LONG).show();
                        WorkerMapFragment mapFragment = new WorkerMapFragment();
                        getActivity().getFragmentManager().popBackStack();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.rel_frag_worker, mapFragment,"findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return v;
    }


}
