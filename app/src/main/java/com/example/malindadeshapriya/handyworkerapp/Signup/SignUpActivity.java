package com.example.malindadeshapriya.handyworkerapp.Signup;

import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.malindadeshapriya.handyworkerapp.R;

public class SignUpActivity extends AppCompatActivity {

    private Button worker,owner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);


        worker = (Button)findViewById(R.id.button_Wroker);
        owner = (Button)findViewById(R.id.button_owner);
        worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HandyWorkerSignUp handyWorkerSignUp = new HandyWorkerSignUp();
                android.support.v4.app.FragmentManager manager =  getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.linearFrag,handyWorkerSignUp,handyWorkerSignUp.getTag()).commit();
;
            }
        });

        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseholdOwnerSignUp householdOwnerSignUp = new HouseholdOwnerSignUp();
                android.support.v4.app.FragmentManager manager =  getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.linearFrag,householdOwnerSignUp,householdOwnerSignUp.getTag()).commit();
            }
        });



    }
}
