package com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.HandyWorkerActivities.WorkerHomeActivity;
import com.example.malindadeshapriya.handyworkerapp.Login.MainActivity;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class workOnProgressFragment extends Fragment {

    View v;
    private Button btn_CompleteWork;
    private RatingBar rb_workerRating;
    double rating = 0;
    String key;
    Bundle bundle;
    String workerid;
    double currentRating=0.0;


    public workOnProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_work_on_progress, container, false);
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

        bundle = getArguments();
        key = bundle.getString("Key");



        btn_CompleteWork = (Button)v.findViewById(R.id.btn_completeWork);
        rb_workerRating = (RatingBar)v.findViewById(R.id.rb_workerRatingAfter);

        btn_CompleteWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                rating = rb_workerRating.getRating();
                if(rating == 0){
                    Toast.makeText(v.getContext(),"Please rate the user",Toast.LENGTH_LONG).show();
                }
                else{

                    final DatabaseReference workRequestRef = FirebaseDatabase.getInstance().getReference("requestWorker").child(key);

                    workRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            workerid = dataSnapshot.child("worker").getValue().toString();
                            DatabaseReference workerRef = FirebaseDatabase.getInstance().getReference("HandyWorkers").child(workerid);
                            workerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    currentRating = Double.parseDouble(dataSnapshot.child("rating").getValue().toString());

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            currentRating = (currentRating+rating)/5;
                            workerRef.child("rating").setValue(currentRating);
                            workerRef.child("status").setValue("available");
                            workRequestRef.child("status").setValue("Completed");
                            Toast.makeText(v.getContext(),"Done",Toast.LENGTH_LONG).show();

                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }



                            MapFragment mapFragment = new MapFragment();
                            getActivity().getFragmentManager().popBackStack();


                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.relativelayout_for_fragment, mapFragment,"findThisFragment")
                                    .addToBackStack(null)
                                    .commit();




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });



        return v;
    }




    private void getWorkerID(String key) {



    }

    public void setWorkerRatings(String id,Double rating) {


    }
}
