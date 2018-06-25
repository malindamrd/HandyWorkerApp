package com.example.malindadeshapriya.handyworkerapp.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.HandyWorkerActivities.WorkerHomeActivity;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.Home;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.ProfileSettingsFragment;
import com.example.malindadeshapriya.handyworkerapp.Manifest;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.example.malindadeshapriya.handyworkerapp.Signup.SignUpActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity{

    private EditText editTextLoginEmail,editTextPassword;
    private Button buttonLogin,buttonSignUp,buttonGoogle;
    private RadioButton worker,owner;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;

    private DatabaseReference mDatabase;

    String email;
    String password;

    int count=0;

    private int LOCATION_PERMISSION_CODE = 1;
    private int STORAGE_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestLocationPermission();
            requestStoragePermission();


        }


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("HouseholdOwners");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser()!=null){
            final String userid = firebaseAuth.getCurrentUser().getUid();
            databaseReference.child("HouseholdOwners").child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren()){
                        String device_token = FirebaseInstanceId.getInstance().getToken();
                        if(count==0){
                            DatabaseReference hos = FirebaseDatabase.getInstance().getReference("HouseholdOwners").child(userid);
                            hos.child("device_token").setValue(device_token);
                            startActivity(new Intent(getApplicationContext(),Home.class));
                            finish();
                            count++;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReference.child("HandyWorkers").child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren()){
                        String device_token = FirebaseInstanceId.getInstance().getToken();
                        if(count==0){
                            DatabaseReference hw = FirebaseDatabase.getInstance().getReference("HandyWorkers").child(userid);
                            hw.child("device_token").setValue(device_token);
                            startActivity(new Intent(getApplicationContext(),WorkerHomeActivity.class));
                            finish();
                            count++;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        editTextLoginEmail = (EditText)findViewById(R.id.editTextLoginEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonSignUp = (Button)findViewById(R.id.buttonSignUp);
        buttonGoogle = (Button)findViewById(R.id.btn_google);


        owner = (RadioButton)findViewById(R.id.radioClient);
        worker = (RadioButton)findViewById(R.id.radioWorker);

        /**
         * Phone Login Button click event
         */
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        /**
         * General Login CLick Event
         */
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String emaill = editTextLoginEmail.getText().toString().trim();
            String passwordd = editTextPassword.getText().toString().trim();

            firebaseAuth.createUserWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String device_token = FirebaseInstanceId.getInstance().getToken();

                        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("HandyWorkers").child(user);
                        userref.child("category").setValue("plumber");
                        userref.child("email").setValue("malinda@hotmail.com");
                        userref.child("name").setValue("Dinushi");
                        userref.child("status").setValue("available");
                        userref.child("phone").setValue("0711234352");
                        userref.child("rating").setValue(0.0);
                        userref.child("image").setValue("null");
                        userref.child("device_token").setValue(device_token);


                        Intent intent = new Intent(MainActivity.this,WorkerHomeActivity.class);
                        startActivity(intent);


                    }
                    else{
                        Toast.makeText(getApplicationContext(),task.toString(),Toast.LENGTH_LONG).show();
                    }
                }


            });

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(MainActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


                        Toast.makeText(MainActivity.this,"Get an error",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

            buttonGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(worker.isChecked()){
                        Toast.makeText(MainActivity.this,"Worker does not allow to sign in using google.",Toast.LENGTH_LONG).show();
                    }
                    if(owner.isChecked()){

                        SignInGoogle();
                    }

                }
            });

    }


    private void SignInGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN ){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);
            }

        }
    }

    private void fireBaseAuthWithGoogle(final GoogleSignInAccount account) {

        progressDialog.setMessage("Login User ...!!!");
        progressDialog.show();


        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Error while logging", Toast.LENGTH_LONG).show();
                }
                else{
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                    if(acct != null){

                        String deivce_token = FirebaseInstanceId.getInstance().getToken();

                        String name = acct.getDisplayName();
                        String email = acct.getEmail();
                        Uri image = acct.getPhotoUrl();

                        String url = image.toString();
                        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();


                        DatabaseReference current_user_db = mDatabase.child(user);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("email").setValue(email);
                        current_user_db.child("image").setValue(url);
                        current_user_db.child("device_token").setValue(deivce_token);
                        Toast.makeText(MainActivity.this,user,Toast.LENGTH_LONG).show();
                    }

                    progressDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this,Home.class);
                    startActivity(intent);

                }
            }
        });
    }

    private void checkLogin(){

          String email = editTextLoginEmail.getText().toString().trim();
          String password = editTextPassword.getText().toString().trim();

          if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Intent intent = new Intent(MainActivity.this,Home.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(MainActivity.this,"Invalid Login attempt",Toast.LENGTH_LONG);
                        }
                    }
                });
          }


    }



    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Location Permission needed")
                    .setMessage("This permission is needed to track your location")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }




    private  void  requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Storage Permission needed")
                    .setMessage("This permission is needed to read and write your storage")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

    }







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                requestStoragePermission();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }









}
