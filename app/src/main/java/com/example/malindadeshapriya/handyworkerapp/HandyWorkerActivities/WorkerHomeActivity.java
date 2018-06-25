package com.example.malindadeshapriya.handyworkerapp.HandyWorkerActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.malindadeshapriya.handyworkerapp.Entity.HandyWorker;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.Home;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.MapFragment;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.NotificationFragment;
import com.example.malindadeshapriya.handyworkerapp.Login.MainActivity;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class WorkerHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;
    TextView textViewWorkerName,textViewPhone;
    DatabaseReference databaseReference;
    ImageView profileImage;

   private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_worker_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getUid();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        WorkerMapFragment mapFragment = new WorkerMapFragment();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rel_frag_worker,mapFragment, mapFragment.getTag()).commit();









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
        getMenuInflater().inflate(R.menu.worker_home, menu);
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
            WorkerMapFragment workerMapFragment = new WorkerMapFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_frag_worker,workerMapFragment,workerMapFragment.getTag()).commit();



        }
        else if(id == R.id.nav_history){

            WorkerHistoryFragment workerHistoryFragment = new WorkerHistoryFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_frag_worker,workerHistoryFragment,workerHistoryFragment.getTag()).commit();



        }else if (id == R.id.nav_manage) {

            WorkerProfileSettings notificationFragment = new WorkerProfileSettings();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.rel_frag_worker,notificationFragment,notificationFragment.getTag()).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(WorkerHomeActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.START);

        textViewWorkerName = (TextView)findViewById(R.id.tv_worker_nev_name);
        textViewPhone = (TextView)findViewById(R.id.tv_worker_nv_phone);
        profileImage = (ImageView)findViewById(R.id.iw_worker_nev_image);



        databaseReference = FirebaseDatabase.getInstance().getReference("HandyWorkers").child(user);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HandyWorker handyWorker = dataSnapshot.getValue(HandyWorker.class);

                String url = handyWorker.getImage();
                textViewWorkerName.setText(handyWorker.getName());
                textViewPhone.setText(handyWorker.getPhone());


                if(!url.equals("null")){
                   // Picasso.with(getApplicationContext()).load(url).resize(200,200).into(profileImage);
                    Picasso.with(getApplicationContext()).load(url)
                            .resize(200, 200)
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
}
