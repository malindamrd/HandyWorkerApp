package com.example.malindadeshapriya.handyworkerapp.Signup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class HouseholdOwnerConfirmActivity extends AppCompatActivity {



    EditText editText_phone,editText_code;
    Button Btn_getCode, Btn_verify, Btn_resend;

    LinearLayout layout;

    String name;
    String email;
    String password;
    String img;

    private String phoneVerificationID;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks ;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_household_owner_confirm);



        layout = (LinearLayout)findViewById(R.id.linear_layout);

        editText_code  = (EditText)findViewById(R.id.editText_code);
        editText_phone = (EditText)findViewById(R.id.editText_phone);

        Btn_getCode    = (Button)findViewById(R.id.btn_getCode);
        Btn_verify     = (Button)findViewById(R.id.btn_verify);
        Btn_resend     = (Button)findViewById(R.id.btn_resend);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        password = bundle.getString("password");
        img = bundle.getString("image");




        firebaseAuth = FirebaseAuth.getInstance();


        Btn_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = editText_phone.getText().toString().trim();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        HouseholdOwnerConfirmActivity.this,
                        mCallbacks

                );
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }
        };

        Btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        
    }



}
