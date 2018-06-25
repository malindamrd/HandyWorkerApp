package com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities;


import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.Control.DistanceCalculate;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkerTrackFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    View v;
    TextView tvWorkerName, tvWorkerStatus;
    ImageButton ibCall, ibCancel;

    Location location;
    Double lati,longi;

    LatLng pp;
    MarkerOptions markerOptions = new MarkerOptions();


    public GoogleMap map;

    Bundle bundle;
    String key;

    ChildEventListener listener = null;

    int latichange = 0;
    int longichange = 0;

    Double lainow =0.0;
    Double longinow=0.0;

    int isCompleted =0;

    DistanceCalculate distanceCalculate = new DistanceCalculate();

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;

    private String workerPhone;


    public WorkerTrackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_worker_track, container, false);

        bundle = getArguments();
        key = bundle.getString("Key");



        tvWorkerName = (TextView) v.findViewById(R.id.tv_wt_nam);
        tvWorkerStatus = (TextView) v.findViewById(R.id.tv_wt_status);
        ibCall = (ImageButton) v.findViewById(R.id.ib_wt_call);
        ibCancel = (ImageButton) v.findViewById(R.id.ib_wt_cancel);


        if(ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(v.getContext(),"Permision not set",Toast.LENGTH_LONG).show();

        }


        LocationManager locationManager = (LocationManager) v.getContext().getSystemService(Context.LOCATION_SERVICE);
        final Boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(isGPSEnable){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,6000,10,this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            lati = location.getLatitude();
            longi = location.getLongitude();

        }
        else{
            Toast.makeText(v.getContext(),"Please enable gps..!!",Toast.LENGTH_LONG).show();
        }


        final DatabaseReference workReq = FirebaseDatabase.getInstance().getReference("requestWorker").child(key);

        workReq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String workerId = dataSnapshot.child("worker").getValue().toString();
                //Toast.makeText(v.getContext(),"Worker ID is :"+workerId,Toast.LENGTH_LONG).show();

                final Query query = FirebaseDatabase.getInstance().getReference("HandyWorkers").child(workerId);


                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvWorkerName.setText(dataSnapshot.child("name").getValue().toString());
                        workerPhone = dataSnapshot.child("phone").getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



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

                            //add
                            map.clear();
                            //add
                            markerOptions.position(pp);
                            map.addMarker(markerOptions);

                            LatLng latLngg = new LatLng(lainow, longinow);
                            markerOptions.position(latLngg);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.person));
                            map.addMarker(markerOptions);
                            latichange = 0;
                            latichange = 0;
                        }

                        Double distance = distanceCalculate.calculateDistance(lainow, longinow, lati, longi);
                        Toast.makeText(v.getContext(), "distance is :" + distance, Toast.LENGTH_LONG).show();
                        if (distance < 0.5) {
                            builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getActivity())
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setSmallIcon(R.drawable.ic_notifications_black_18dp)
                                    .setContentTitle("Yo Worker Notification")
                                    .setContentText("Your worker has arrived")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                            notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                            notificationManager.notify(1, builder.build());

                            query.removeEventListener(listener);


                            Bundle bundle1 = new Bundle();
                            bundle1.putString("Key", key);


                            workOnProgressFragment frag = new workOnProgressFragment();
                            frag.setArguments(bundle1);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.relativelayout_for_fragment, frag, "findThisFragment")
                                    .addToBackStack(null)
                                    .commit();

                            isCompleted = 1;


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



        ibCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+workerPhone));
                startActivity(intent);
            }
        });









        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =  (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(v.getContext(),R.raw.style_json);
        map.setMapStyle(styleOptions);
        pp = new LatLng(lati, longi);


        markerOptions.position(pp).title("Home");
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,15.5f));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pp,15.5f));


    }
}
