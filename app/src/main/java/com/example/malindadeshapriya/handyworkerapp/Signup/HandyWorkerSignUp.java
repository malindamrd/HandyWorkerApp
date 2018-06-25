package com.example.malindadeshapriya.handyworkerapp.Signup;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandyWorkerSignUp extends Fragment {



    private EditText worker_name,worker_email,worker_password,worker_cpassword,worker_emgno;
    private Spinner worker_category;
    private Button btn_signup;

    String name,email,passowrd,cpassword,emgno,category;

    public HandyWorkerSignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_handy_worker_sign_up, container, false);

        worker_name = (EditText)v.findViewById(R.id.et_worker_name);
        worker_email = (EditText)v.findViewById(R.id.et_worker_email);
        worker_password = (EditText)v.findViewById(R.id.et_worker_password);
        worker_cpassword = (EditText)v.findViewById(R.id.et_worker_cpassword);
        worker_emgno = (EditText)v.findViewById(R.id.et_worker_emgcontact);
        worker_category = (Spinner)v.findViewById(R.id.spinner_category);

        btn_signup = (Button)v.findViewById(R.id.btn_signup);


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = worker_name.getText().toString().trim();
                email = worker_email.getText().toString().trim();
                passowrd = worker_password.getText().toString().trim();
                cpassword = worker_cpassword.getText().toString().trim();
                emgno = worker_emgno.getText().toString().trim();
                category = worker_category.getSelectedItem().toString();


                if(!name.isEmpty() && !email.isEmpty() && !passowrd.isEmpty() && !cpassword.isEmpty() && !emgno.isEmpty() && !category.isEmpty()){

                    if(passowrd.equals(cpassword)){

                        Intent intent = new Intent(v.getContext(),WorkerPhoneConfirm.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",name);
                        bundle.putString("email",email);
                        bundle.putString("password",passowrd);
                        bundle.putString("category",category);
                        bundle.putString("emg",emgno);
                        bundle.putString("image","url");

                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(v.getContext(),"Confirm password in different",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });





        // Inflate the layout for this fragment
        return v;
    }

}
