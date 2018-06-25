package com.example.malindadeshapriya.handyworkerapp.HandyWorkerActivities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.Control.RequestHistory;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.NotificationFragment;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkerHistoryFragment extends Fragment {


    View v;
    FirebaseAuth firebaseAuth;
    String user;
    DatabaseReference databaseReference;
    private RecyclerView historyList;

    String worker;
    String owner;
    String name,email;

    DatabaseReference ownerRef;

    public WorkerHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v = inflater.inflate(R.layout.fragment_worker_history, container, false);

       firebaseAuth = FirebaseAuth.getInstance();
       user = firebaseAuth.getCurrentUser().getUid();

       databaseReference = FirebaseDatabase.getInstance().getReference("requestWorker");
       historyList = (RecyclerView)v.findViewById(R.id.history_list_worker);
       historyList.setHasFixedSize(true);
       historyList.setLayoutManager(new LinearLayoutManager(v.getContext()));


       return v;
    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<RequestHistory,NotificationFragment.HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RequestHistory, NotificationFragment.HistoryViewHolder>(
                RequestHistory.class,
                R.layout.history_row_worker,
                NotificationFragment.HistoryViewHolder.class,
                databaseReference


        ) {
            @Override
            protected void populateViewHolder(final NotificationFragment.HistoryViewHolder viewHolder, final RequestHistory model, int position) {



                worker = model.getWorker();

                if(worker.equals(user)){
                    ownerRef = FirebaseDatabase.getInstance().getReference("HouseholdOwners").child(model.getOwner());
                    ownerRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            name = dataSnapshot.child("name").getValue().toString();
                            email = dataSnapshot.child("email").getValue().toString();

                            viewHolder.setWorkerDate("Request Date : "+model.getDate());
                            viewHolder.setWorkerName("Worker Name : "+name);
                            viewHolder.setWorkerStatus("Request Status : "+model.getStatus());
                            viewHolder.setWorkerTime("Request Time  : "+model.getTime());
                            viewHolder.setWorkerType("Email : "+email);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        };

        historyList.setAdapter(firebaseRecyclerAdapter );
    }
}
