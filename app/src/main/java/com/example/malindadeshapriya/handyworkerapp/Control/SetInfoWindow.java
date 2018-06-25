package com.example.malindadeshapriya.handyworkerapp.Control;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.Entity.Sample;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.MapFragment;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.workOnProgressFragment;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by malindadeshapriya on 5/13/18.
 */

public class SetInfoWindow {

    Context v;
    Sample sample;
    double lati;
    double longi;
    String ownerID;
    public GoogleMap map;

    DatabaseReference requestReference;

    ProgressDialog progressDialog;

    ChildEventListener listener1 = null;
    ChildEventListener listener = null;

    int latichange = 0;
    int longichange = 0;

    Double lainow =0.0;
    Double longinow=0.0;

    int isCompleted =0;

    long catCount = 0;

    Activity activity;

    MarkerOptions markerOptions = new MarkerOptions();

    DistanceCalculate calculater = new DistanceCalculate();
    DistanceCalculate distanceCalculate = new DistanceCalculate();

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;


    public SetInfoWindow() {
    }

    public SetInfoWindow(Context v, Sample sample, double lati, double longi, String ownerID, GoogleMap map, Activity activity) {
        this.v = v;
        this.sample = sample;
        this.lati = lati;
        this.longi = longi;
        this.ownerID = ownerID;
        this.map = map;
        this.activity = activity;
    }

    public void setWindow(){
        progressDialog = new ProgressDialog(v);

        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(v,sample.getName(),Double.toString(sample.getDistance()),sample.getPhone(),sample.getEmail(),sample.getImage()));
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                DateFormat df = new SimpleDateFormat("K:mm a, z");
                String time = df.format(Calendar.getInstance().getTime());


                requestReference = FirebaseDatabase.getInstance().getReference().child("requestWorker");
                final String key = requestReference.push().getKey();
                DatabaseReference db = requestReference.child(key);
                db.child("lati").setValue(lati);
                db.child("longi").setValue(longi);
                db.child("status").setValue("Not confirmed");
                db.child("Owner").setValue(FirebaseAuth.getInstance().getUid());
                db.child("distance").setValue(sample.getDistance());
                db.child("worker").setValue(sample.getSampleID());
                db.child("date").setValue(date);
                db.child("time").setValue(time);


                progressDialog.setMessage("Pending Approvel ...!!!");
                progressDialog.show();

                final DatabaseReference workReq = FirebaseDatabase.getInstance().getReference("requestWorker").child(key);

                final Query query1 =  FirebaseDatabase.getInstance().getReference("requestWorker").child(key);

                listener1 = query1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        progressDialog.dismiss();
                        if(dataSnapshot.getValue().toString().equals("Accepted")){

                            workReq.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String workerId = dataSnapshot.child("worker").getValue().toString();
                                    //Toast.makeText(v.getContext(),"Worker ID is :"+workerId,Toast.LENGTH_LONG).show();




                                    final Query query = FirebaseDatabase.getInstance().getReference("HandyWorkers").child(workerId);

                                    listener = query.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                            if (dataSnapshot.getKey().equals("lati")) {
                                                lainow = Double.parseDouble(dataSnapshot.getValue().toString());
                                                //Toast.makeText(v.getContext(),"lati changed : "+lainow,Toast.LENGTH_LONG).show();
                                                latichange = 1;
                                            }
                                            if (dataSnapshot.getKey().equals("longi")) {
                                                longinow = Double.parseDouble(dataSnapshot.getValue().toString());
                                                //Toast.makeText(v.getContext(),"longi changed : "+longinow,Toast.LENGTH_LONG).show();
                                                longichange = 1;
                                            }
                                            //Toast.makeText(v.getContext(),lainow+" : "+longinow,Toast.LENGTH_LONG).show();
                                            if (latichange == 1 && longichange == 1) {
                                                LatLng latLngg = new LatLng(lainow, longinow);
                                                markerOptions.position(latLngg);
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.person));
                                                map.addMarker(markerOptions);
                                                latichange = 0;
                                                latichange = 0;
                                            }

                                            Double distance = distanceCalculate.calculateDistance(lainow, longinow, lati, longi);
                                            Toast.makeText(v, "distance is :" + distance, Toast.LENGTH_LONG).show();
                                            if (distance < 0.5) {
                                                builder = (NotificationCompat.Builder) new NotificationCompat.Builder(activity)
                                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                                        .setSmallIcon(R.drawable.ic_notifications_black_18dp)
                                                        .setContentTitle("Yo Worker Notification")
                                                        .setContentText("Your worker has arrived")
                                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                                                notificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
                                                notificationManager.notify(1, builder.build());


                                                /*Bundle bundle1 = new Bundle();
                                                bundle1.putString("Key", key);
                                                workOnProgressFragment frag = new workOnProgressFragment();
                                                frag.setArguments(bundle1);
                                                activity.getFragmentManager().beginTransaction()
                                                        .replace(R.id.relativelayout_for_fragment, frag, "findThisFragment")
                                                        .addToBackStack(null)
                                                        .commit();

                                                isCompleted = 1;

                                                query.removeEventListener(listener);
                                                query1.removeEventListener(listener1);
                                                */

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



                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            /*MapFragment mapFragment = new MapFragment();
                            activity.getFragmentManager().beginTransaction()
                                    .replace(R.id.relativelayout_for_fragment, mapFragment,"findThisFragment")
                                    .addToBackStack(null)
                                    .commit();*/
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
            }
        });

    }




}
