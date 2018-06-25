package com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities;


import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.Control.CustomInfoWindowAdapter;
import com.example.malindadeshapriya.handyworkerapp.Control.DistanceCalculate;
import com.example.malindadeshapriya.handyworkerapp.Control.SetSearchCount;
import com.example.malindadeshapriya.handyworkerapp.Entity.HandyWorker;
import com.example.malindadeshapriya.handyworkerapp.Entity.Sample;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,LocationListener{

    public GoogleMap map;
    Button buttonmenue,btnSearch1,btnSearch2,btnSearch3;
    Location location;
    Double lati;
    Double longi;
    Double latii;
    Double longii;
    private LatLng pickupLocation;
    EditText searchText;
    View v;
    DatabaseReference databaseReference;
    DistanceCalculate calculater = new DistanceCalculate();
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userref;
    String user;

    MarkerOptions markerOptions = new MarkerOptions();
    DatabaseReference requestReference;
    DatabaseReference workerRequestRef;
    DatabaseReference workerref;
    ProgressDialog progressDialog;

    int latichange = 0;
    int longichange = 0;

    Double lainow =0.0;
    Double longinow=0.0;

    int isCompleted =0;

    long catCount = 0;

    LatLng pp;

    ArrayList<HandyWorker> workerList = new ArrayList<HandyWorker>();


    int in;
    DistanceCalculate distanceCalculate = new DistanceCalculate();

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;

    private Context context;

    ChildEventListener listener = null;
    ChildEventListener listener1 = null;

    String count[][];



    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v = inflater.inflate(R.layout.fragment_map, container, false);
        //disable back button
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    return true;
                } else {
                    return false;
                }
            }
        });




        btnSearch1 = (Button)v.findViewById(R.id.btnSearch1);
        btnSearch2 = (Button)v.findViewById(R.id.btnSearch2);
        btnSearch3 = (Button)v.findViewById(R.id.btnSearch3);

        /**
         * Get count
         */
        Query query = FirebaseDatabase.getInstance().getReference("SearchCount");
        ValueEventListener listener5;

        listener5 = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                catCount = dataSnapshot.getChildrenCount();
                int cc = (int)catCount;
                count= new String[cc][2];
                int i=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    count[i][0] = ds.getKey().toString();
                    count[i][1] = ds.child("count").getValue().toString();
                    i++;

                }
                Arrays.sort(count, new Comparator<String[]>() {
                    @Override
                    public int compare(String[] o1, String[] o2) {
                        return Double.valueOf(o2[1]).compareTo(
                                Double.valueOf(o1[1]));
                    }
                });
                btnSearch1.setText(count[0][0]);
                btnSearch2.setText(count[1][0]);
                btnSearch3.setText(count[2][0]);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       firebaseAuth = FirebaseAuth.getInstance();
       user = firebaseAuth.getCurrentUser().getUid();
       databaseReference = FirebaseDatabase.getInstance().getReference();
       userref = FirebaseDatabase.getInstance().getReference().child("HouseholdOwners").child(user);
        progressDialog = new ProgressDialog(v.getContext());

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

       buttonmenue = (Button) v.findViewById(R.id.buttonMenu);
       searchText = (EditText)v.findViewById(R.id.etSearch);

       buttonmenue.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ((Home) getActivity()).openDrawer();
           }
       });

       final MarkerOptions markerOptions = new MarkerOptions();

       searchText.setOnKeyListener(new View.OnKeyListener() {

           @Override
           public boolean onKey(final View v, int keyCode, KeyEvent event) {
               if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                       (keyCode == KeyEvent.KEYCODE_ENTER)) {

                   try {
                       InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                       imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                   } catch (Exception e) {
                       Toast.makeText(v.getContext(),"Error :"+e,Toast.LENGTH_LONG).show();
                   }
                   String searchkey = searchText.getText().toString().trim();

                   SetSearchCount setSearchCount = new SetSearchCount(searchkey);
                   setSearchCount.setCount();
                   map.clear();
                   LatLng pp = new LatLng(lati, longi);
                   MarkerOptions mp = new MarkerOptions();
                   mp.position(pp).title("Home");
                   map.addMarker(mp);
                   map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
                   map.animateCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));


                   searchWorker(searchkey);










                   /*final Query query = databaseReference.child("HandyWorkers").orderByChild("category").equalTo(searchkey);
                   query.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(final DataSnapshot dataSnapshot) {
                           if(dataSnapshot.exists()){

                               for (final DataSnapshot issue : dataSnapshot.getChildren()) {

                                   if(issue.child("status").getValue().toString().equals("available")){

                                       latii =  Double.parseDouble(issue.child("lati").getValue().toString());
                                       longii =  Double.parseDouble(issue.child("longi").getValue().toString());

                                       String phone = issue.child("phone").getValue().toString();
                                       String email = issue.child("email").getValue().toString();
                                       String imageur = issue.child("image").getValue().toString();
                                       Double distance = calculater.calculateDistance(latii,longii,lati,longi);
                                       final Double round = calculater.round(distance,2);
                                       final LatLng pp = new LatLng(latii, longii);

                                       markerOptions.position(pp).title(issue.child("name")
                                               .getValue().toString())
                                               .snippet(round.toString()+" KM");
                                       markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.person));
                                       map.addMarker(markerOptions);

                                       map.setInfoWindowAdapter(new CustomInfoWindowAdapter(v.getContext(),issue.child("name").getValue().toString(),round.toString(),phone,email,imageur));
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
                                               db.child("distance").setValue(round);
                                               db.child("worker").setValue(issue.getKey());
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
                                                                                  Bundle bundle1 = new Bundle();
                                                                                  bundle1.putString("Key", key);
                                                                                  workOnProgressFragment frag = new workOnProgressFragment();
                                                                                  frag.setArguments(bundle1);
                                                                                  getActivity().getSupportFragmentManager().beginTransaction()
                                                                                          .replace(R.id.relativelayout_for_fragment, frag, "findThisFragment")
                                                                                          .addToBackStack(null)
                                                                                          .commit();

                                                                                  isCompleted = 1;

                                                                                  query.removeEventListener(listener);
                                                                                  query1.removeEventListener(listener1);


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
                                                           MapFragment mapFragment = new MapFragment();
                                                           getActivity().getSupportFragmentManager().beginTransaction()
                                                                   .replace(R.id.relativelayout_for_fragment, mapFragment,"findThisFragment")
                                                                   .addToBackStack(null)
                                                                   .commit();
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
                           }
                           else{

                               Toast.makeText(v.getContext(),"No workers found..!!!",Toast.LENGTH_LONG).show();
                           }
                       }
                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });*/
                   return true;
               }
               return false;
           }
       });

       btnSearch1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View v) {
               String searchkey = btnSearch1.getText().toString();

               SetSearchCount setSearchCount = new SetSearchCount(searchkey);
               setSearchCount.setCount();
               map.clear();

               LatLng pp = new LatLng(lati, longi);
               MarkerOptions mp = new MarkerOptions();
               mp.position(pp).title("Home");
               map.addMarker(mp);
               map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
               map.animateCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));


               searchWorker(searchkey);

           }
       });

       btnSearch2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View v) {
               String searchkey = btnSearch2.getText().toString();

               SetSearchCount setSearchCount = new SetSearchCount(searchkey);
               setSearchCount.setCount();
               map.clear();

               LatLng pp = new LatLng(lati, longi);
               MarkerOptions mp = new MarkerOptions();
               mp.position(pp).title("Home");
               map.addMarker(mp);
               map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
               map.animateCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));


               searchWorker(searchkey);

           }
       });

        btnSearch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String searchkey = btnSearch3.getText().toString();

                SetSearchCount setSearchCount = new SetSearchCount(searchkey);
                setSearchCount.setCount();
                map.clear();

                LatLng pp = new LatLng(lati, longi);
                MarkerOptions mp = new MarkerOptions();
                mp.position(pp).title("Home");
                map.addMarker(mp);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));

                searchWorker(searchkey);
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
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(v.getContext(),R.raw.style_json);
        map.setMapStyle(styleOptions);

        //took initializer to top
        pp = new LatLng(lati, longi);

        Circle circle = map.addCircle(new CircleOptions()
        .center(pp)
        .radius(1250)
        .strokeColor(Color.TRANSPARENT)
        .fillColor(R.color.wallet_bright_foreground_holo_light)
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
        markerOptions.position(pp).title("Home");
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
    }

    @Override
    public void onLocationChanged(Location location) {
        Double latin = location.getLatitude();
        Double longin = location.getLongitude();
        userref.child("lati").setValue(latin);
        userref.child("longi").setValue(longin);
        Toast.makeText(v.getContext(),"Added",Toast.LENGTH_LONG).show();

        //added

        Toast.makeText(v.getContext(),"This triggered",Toast.LENGTH_LONG).show();

        map.clear();
        LatLng pp = new LatLng(latin,longin);
        markerOptions.position(pp).title("Home");
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pp,13.5f));
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


    public void searchWorker(String searchKey){
        final Query query1 =  databaseReference.child("HandyWorkers").orderByChild("category").equalTo(searchKey);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (final DataSnapshot issue : dataSnapshot.getChildren()) {

                        if(issue.child("status").getValue().toString().equals("available")) {

                            HandyWorker handyWorker = issue.getValue(HandyWorker.class);
                            handyWorker.setSampleID(issue.getKey());

                            double latiOfWorker = handyWorker.getLati();
                            double longiOfWorker = handyWorker.getLongi();

                            Double distance = calculater.calculateDistance(latiOfWorker, longiOfWorker, lati, longi);
                            final double roundDistance = calculater.round(distance, 2);
                            handyWorker.setDistance(roundDistance);

                            workerList.add(handyWorker);
                        }
                    }
                    double currentNearUserIndex=0;
                    String currentNearUserID = null;

                    for(HandyWorker s : workerList){
                        double latiOfWorker = s.getLati();
                        double longiOfWorker = s.getLongi();
                        double ratings = s.getRating();
                        Double distance = calculater.calculateDistance(latiOfWorker,longiOfWorker,lati,longi);
                        final double roundDistance = calculater.round(distance,2);
                        double index = roundDistance/ratings;

                        if(currentNearUserIndex == 0){
                            currentNearUserIndex = index;
                            currentNearUserID = s.getSampleID();
                        }
                        else if(index < currentNearUserIndex){
                            currentNearUserIndex = index;
                            currentNearUserID = s.getSampleID();
                        }
                        else{
                        }
                    }


                    for(final HandyWorker s: workerList){

                        double latiOfWorker = s.getLati();
                        double longiOfWorker = s.getLongi();
                        Double distance = calculater.calculateDistance(latiOfWorker,longiOfWorker,lati,longi);
                        final double roundDistance = calculater.round(distance,2);

                        if(s.getSampleID().equals(currentNearUserID)){
                            LatLng ppp = new LatLng(latiOfWorker,longiOfWorker);
                            markerOptions.position(ppp).title(s.getName());
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.engineer_good));
                            Toast.makeText(v.getContext(),s.getEmail(),Toast.LENGTH_LONG).show();
                            map.addMarker(markerOptions);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ppp,13.5f));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ppp,13.5f));

                        }
                        else {
                            LatLng pppp = new LatLng(latiOfWorker, longiOfWorker);
                            markerOptions.position(pppp).title(s.getName());
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.person));
                            map.addMarker(markerOptions);
                        }

                        final String id = s.getSampleID();




                        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(v.getContext(),id,Double.toString(s.getDistance()),s.getPhone(),s.getEmail(),s.getImage()));
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
                                db.child("distance").setValue(s.getDistance());
                                db.child("worker").setValue(id);
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

                                            //added
                                            Bundle bundle1 = new Bundle();
                                            bundle1.putString("Key", key);
                                            WorkerTrackFragment frag = new WorkerTrackFragment();
                                            frag.setArguments(bundle1);
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.relativelayout_for_fragment, frag, "findThisFragment")
                                                    .addToBackStack(null)
                                                    .commit();








                                            /*
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

                                                                //add
                                                                map.clear();
                                                                //add
                                                                markerOptions.position(pp);

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
                                                                Bundle bundle1 = new Bundle();
                                                                bundle1.putString("Key", key);


                                                                workOnProgressFragment frag = new workOnProgressFragment();
                                                                frag.setArguments(bundle1);
                                                                getActivity().getSupportFragmentManager().beginTransaction()
                                                                        .replace(R.id.relativelayout_for_fragment, frag, "findThisFragment")
                                                                        .addToBackStack(null)
                                                                        .commit();

                                                                isCompleted = 1;

                                                                query.removeEventListener(listener);
                                                                query1.removeEventListener(listener1);

                                                                //workReq.removeEventListener(null);
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

                                                    //query.removeEventListener(listener);
                                                    //query1.removeEventListener(listener1);
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });*/
                                        }
                                        else{
                                            MapFragment mapFragment = new MapFragment();
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.relativelayout_for_fragment, mapFragment,"findThisFragment")
                                                    .addToBackStack(null)
                                                    .commit();
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

                }else{
                    Toast.makeText(v.getContext(),"No Workers Found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
