package com.example.malindadeshapriya.handyworkerapp.Signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.HandyWorkerActivities.WorkerHomeActivity;
import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.Home;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class WorkerPhoneConfirm extends AppCompatActivity {

    private  String name,email,password,emgno,category,imageurl;
    private Button btn_getCode,btn_resendCode,btn_verify;
    private EditText phoneNo,confirmCode;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String verificationID;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_phone_confirm);


        //Get details from previous fragment
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        password = bundle.getString("password");
        imageurl = bundle.getString("image");
        category = bundle.getString("category");
        emgno = bundle.getString("emg");

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("HandyWorkers");


        btn_getCode = (Button)findViewById(R.id.btn_getCode);
        btn_resendCode = (Button)findViewById(R.id.btn_resend);
        btn_verify = (Button)findViewById(R.id.btn_verify);

        phoneNo = (EditText)findViewById(R.id.editText_phone);
        confirmCode = (EditText)findViewById(R.id.editText_code);




        btn_getCode.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                String phone = phoneNo.getText().toString().trim();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,
                        60,
                        TimeUnit.SECONDS,
                        WorkerPhoneConfirm.this,
                        mCallbacks

                );
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(WorkerPhoneConfirm.this,"Check inbox for OTP",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(WorkerPhoneConfirm.this,"IN Valid phone number",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationID = s;
                resendingToken = forceResendingToken;

            }
        };


        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String verificationCode = confirmCode.getText().toString().trim();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, verificationCode);
                    signInWithPhoneAuthCredentials(credential);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Invalid Vode",Toast.LENGTH_LONG).show();
                }


            }
        });

    }



    private void signInWithPhoneAuthCredentials(PhoneAuthCredential credential){

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                            if(!task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_LONG).show();
                                confirmCode.setText("");

                            }
                            else {

                                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference current_user_db = mDatabase.child(user);

                                current_user_db.child("name").setValue(name);
                                current_user_db.child("email").setValue(email);
                                current_user_db.child("password").setValue(password);
                                current_user_db.child("image").setValue(imageurl);
                                current_user_db.child("category").setValue(category);
                                current_user_db.child("status").setValue("not available");
                                current_user_db.child("phone").setValue(emgno);
                                current_user_db.child("rating").setValue(0.0);


                                Intent intent = new Intent(WorkerPhoneConfirm.this, WorkerHomeActivity.class);
                                startActivity(intent);

                            }


                    }


                });





    }





}
