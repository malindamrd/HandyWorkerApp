package com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.Control.RequestHistory;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private RecyclerView historyList;
    static View v;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    String user;

    DatabaseReference databaseReference;
    DatabaseReference workerRef;

    String name;
    String type;

    String worker;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_notification, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("requestWorker");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();

        historyList = (RecyclerView)v.findViewById(R.id.history_list);
        historyList.setHasFixedSize(true);
        historyList.setLayoutManager(new LinearLayoutManager(v.getContext()));



        return v;
    }

    @Override
    public void onStart() {
        super.onStart();



        FirebaseRecyclerAdapter<RequestHistory,HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RequestHistory, HistoryViewHolder>(
                RequestHistory.class,
                R.layout.history_row,
                HistoryViewHolder.class,
                databaseReference


        ) {
            @Override
            protected void populateViewHolder(final HistoryViewHolder viewHolder, final RequestHistory model, int position) {



                worker = model.getWorker();
                Toast.makeText(v.getContext(),worker,Toast.LENGTH_LONG).show();

                workerRef = FirebaseDatabase.getInstance().getReference("HandyWorkers").child(worker);

                workerRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       name = dataSnapshot.child("name").getValue().toString();
                       type = dataSnapshot.child("category").getValue().toString();


                        viewHolder.setWorkerDate("Request Date : "+model.getDate());
                        viewHolder.setWorkerName("Worker Name : "+name);
                        viewHolder.setWorkerStatus("Request Status : "+model.getStatus());
                        viewHolder.setWorkerTime("Request Time  : "+model.getTime());
                        viewHolder.setWorkerType("Worker Type : "+type);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                /*
                viewHolder.setWorkerDate("<mamama");
                viewHolder.setWorkerName(name);
                viewHolder.setWorkerStatus("ddffd");
                viewHolder.setWorkerTime("dfdf");
                viewHolder.setWorkerType(type);

                */


            }
        };


        historyList.setAdapter(firebaseRecyclerAdapter );
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setWorkerName(String name){
            TextView workerName = (TextView)mView.findViewById(R.id.worker_name);
            workerName.setText(name);
        }

        public void setWorkerType(String type){
            TextView workerType = (TextView)mView.findViewById(R.id.worker_type);
            workerType.setText(type);
        }

        public void setWorkerDate(String date){
            TextView workerDate = (TextView)mView.findViewById(R.id.worker_date);
            workerDate.setText(date);
        }

        public void setWorkerTime(String time){
            TextView workerTime = (TextView)mView.findViewById(R.id.worker_time);
            workerTime.setText(time);
        }

        public void setWorkerStatus(String status){
            TextView workerStatus = (TextView)mView.findViewById(R.id.worker_status);
            workerStatus.setText(status);
        }




    }


}
