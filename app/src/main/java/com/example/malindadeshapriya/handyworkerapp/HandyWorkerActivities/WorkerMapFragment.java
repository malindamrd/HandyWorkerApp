package com.example.malindadeshapriya.handyworkerapp.HandyWorkerActivities;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.Entity.WorkerRequest;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
public class WorkerMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    GoogleMap map;
    Button menuButton;
    Switch status_switch;

    FirebaseAuth firebaseAuth;

    private String workerid;


    static final int LOCATION_PERMISION = 1;
    LocationManager locationManager;
    LocationListener locationListener;

    Location location;

    private String workerStatus;

    private Double lati = 6.0;
    private Double longi = 2.9;

    private WorkerRequest workerRequest;
    private String owner = null;
    private String status = null;
    private Double distance = 0.0;
    Double latiReq = 0.0;
    Double longiReq = 0.0;
    String requestKey;

    //Double currentLati;
    //Double currentLongi;

    DatabaseReference handworkerRef = FirebaseDatabase.getInstance().getReference("HandyWorkers");
    private String user;
    DatabaseReference userref;
    DatabaseReference statusref;
    DatabaseReference nw;
    DatabaseReference workerReqRef = FirebaseDatabase.getInstance().getReference("requestWorker");
    DatabaseReference dbb = FirebaseDatabase.getInstance().getReference("requestWorker");
    View v;
    MarkerOptions markerOptions = new MarkerOptions();



    public WorkerMapFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_worker_map, container, false);
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

        userref = handworkerRef.child(user);
        statusref = userref.child("status");



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

            userref.child("lati").setValue(lati);
            userref.child("longi").setValue(longi);

        }
        else{
            Toast.makeText(v.getContext(),"Please enable gps..!!",Toast.LENGTH_LONG).show();
        }


        status_switch = (Switch)v.findViewById(R.id.switch_status);

        statusref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                workerStatus = dataSnapshot.getValue(String.class);
                if(workerStatus!=null&&!workerStatus.isEmpty()&&!workerStatus.equals("null")&&workerStatus.equals("available")){
                    status_switch.setChecked(true);
                }
                else{
                    status_switch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        status_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String status = "available";
                    userref.child("status").setValue(status);
                }
                else{
                    String status = "not available";
                    userref.child("status").setValue(status);
                }
            }
        });

        menuButton = (Button)v.findViewById(R.id.buttonMenuWorker);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WorkerHomeActivity) getActivity()).openDrawer();
            }
        });

        dbb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot1, String s) {
                requestKey = dataSnapshot1.getKey();
                DatabaseReference db = dbb.child(requestKey);
                db.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        owner = dataSnapshot.child("Owner").getValue(String.class);
                        workerid = dataSnapshot.child("worker").getValue(String.class);
                        if (workerid!=null&&!workerid.isEmpty()&&!workerid.equals("null")&&workerid.equals(user))
                        {
                            latiReq = dataSnapshot.child("lati").getValue(Double.class);
                            longiReq = dataSnapshot.child("longi").getValue(Double.class);
                            distance = dataSnapshot.child("distance").getValue(Double.class);
                            status = dataSnapshot.child("status").getValue(String.class);

                            if(status!=null&&!status.isEmpty()&&!status.equals("null")&&status.equals("Not confirmed")){
                                Bundle bundle = new Bundle();
                                bundle.putDouble("distance",distance);
                                bundle.putString("owner",owner);
                                bundle.putString("worker",workerid);
                                bundle.putString("status",status);
                                bundle.putDouble("lati",latiReq);
                                bundle.putDouble("longi",longiReq);
                                bundle.putString("requestKey",requestKey);

                                //bundle.putString("key",requestKey);
                                CustomerCall customerCall = new CustomerCall();

                                customerCall.setArguments(bundle);

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.rel_frag_worker, customerCall,"findThisFragment")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =  (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map_worker);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng pp = new LatLng(lati, longi);
        markerOptions.position(pp).title("Home");
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,15.5f));

        userref.child("lati").setValue(lati);
        userref.child("longi").setValue(longi);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double currentLati = location.getLatitude();
        Double currentLongi = location.getLongitude();
        userref.child("lati").setValue(location.getLatitude());
        userref.child("longi").setValue(location.getLongitude());
        Toast.makeText(v.getContext(),"Change",Toast.LENGTH_LONG).show();


        //added

        map.clear();
        LatLng pp = new LatLng(currentLati,currentLongi);
        markerOptions.position(pp).title("Home");
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,15.5f));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pp,15.5f));
        Log.e("Ma","Ma");
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
}
