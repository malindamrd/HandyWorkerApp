package com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.malindadeshapriya.handyworkerapp.Entity.HandyWorker;
import com.example.malindadeshapriya.handyworkerapp.Entity.HouseholdOwner;
import com.example.malindadeshapriya.handyworkerapp.Login.MainActivity;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button button;
    GoogleMap map;
    private String status = "notlog";



    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mref;

    private Bitmap bitmap;

    ImageView profileImage;
    TextView textViewName, textViewEmail;
    private String user;

    DatabaseReference databaseReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();

        button = (Button)findViewById(R.id.buttonMenu);




        String user = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference householdOwnersRef = FirebaseDatabase.getInstance().getReference("HouseholdOwners");
        DatabaseReference userref = householdOwnersRef.child(user);
        DatabaseReference imageref = userref.child("image");
        DatabaseReference phonered = userref.child("phone");



        if(phonered == null || phonered.equals("")){
            ProfileSettingsFragment profileSettingsFragment = new ProfileSettingsFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment,profileSettingsFragment, profileSettingsFragment.getTag()).commit();

        }
        else{
            MapFragment mapFragment = new MapFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment,mapFragment, mapFragment.getTag()).commit();

        }



    }



    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.START);

        profileImage = (ImageView)findViewById(R.id.iv_ho_nev_image);
        textViewName = (TextView)findViewById(R.id.tv_ho_nev_name);
        textViewEmail = (TextView)findViewById(R.id.tv_ho_nev_email);

        databaseReference = FirebaseDatabase.getInstance().getReference("HouseholdOwners").child(user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HouseholdOwner householdOwner = dataSnapshot.getValue(HouseholdOwner.class);

                String url = householdOwner.getImage();

                textViewEmail.setText(householdOwner.getEmail());
                textViewName.setText(householdOwner.getName());
                if(!url.equals("null")){
                    //Picasso.with(getApplicationContext()).load(url).resize(200,200).into(profileImage);
                    Picasso.with(getApplicationContext()).load(url)
                            .resize(220, 220)
                            .into(profileImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    profileImage.setImageDrawable(imageDrawable);
                                }
                                @Override
                                public void onError() {
                                    //prof.setImageResource(R.drawable.default_image);
                                }
                            });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            MapFragment mapFragment = new MapFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment,mapFragment, mapFragment.getTag()).commit();

        } else if (id == R.id.notifications) {
            NotificationFragment notificationFragment = new NotificationFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment,notificationFragment,notificationFragment.getTag()).commit();

        } else if (id == R.id.profile_settings) {
            ProfileSettingsFragment contactUsFragment = new ProfileSettingsFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment,contactUsFragment, contactUsFragment.getTag()).commit();

        } else if (id == R.id.about_us) {
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment,aboutUsFragment, aboutUsFragment.getTag()).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}
