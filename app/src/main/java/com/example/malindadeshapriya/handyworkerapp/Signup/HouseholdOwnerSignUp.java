package com.example.malindadeshapriya.handyworkerapp.Signup;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities.Home;
import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseholdOwnerSignUp extends Fragment {


    private Button ownerSignUp;
    private EditText Ownername,Owneremail,Ownerpassword,OwnerconfirmPassword,OwneremergencyNo;

    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog progressDialog;

    public HouseholdOwnerSignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View vm = inflater.inflate(R.layout.fragment_household_owner_sign_up, container, false);

        //Firebase Auth initialization
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("HouseholdOwners");

        progressDialog = new ProgressDialog(vm.getContext());


        //check whether user has registered
        if(firebaseAuth.getCurrentUser()!=null){

            Intent intent = new Intent(vm.getContext(),Home.class);
            startActivity(intent);

        }


        //Edit Text Initialization
        Ownername = (EditText)vm.findViewById(R.id.txt_owner_name);
        Owneremail = (EditText)vm.findViewById(R.id.txt_owner_email);
        Ownerpassword = (EditText)vm.findViewById(R.id.et_worker_password);
        OwnerconfirmPassword = (EditText)vm.findViewById(R.id.txt_owner_confirm);

        OwneremergencyNo = (EditText)vm.findViewById(R.id.txt_owner_emg);

        //Button Initialization
        ownerSignUp = (Button)vm.findViewById(R.id.btn_signup_owner);


        ownerSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRegister(vm);


            }
        });




        return vm;
    }


    public void startRegister(final View v){

         String name = Ownername.getText().toString().trim();
        final String email = Owneremail.getText().toString().trim();
        final String password = Ownerpassword.getText().toString().trim();
        final String conPass = OwnerconfirmPassword.getText().toString().trim();

        final String emgCon = OwneremergencyNo.getText().toString().trim();

        if(firebaseAuth.getCurrentUser()==null){

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
                    !TextUtils.isEmpty(conPass) &&!TextUtils.isEmpty(emgCon)){


                if(password.equals(conPass)){

                    /*progressDialog.setMessage("Signing up...");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String user_id = firebaseAuth.getCurrentUser().getUid();

                                DatabaseReference current_user_db = mDatabase.child(user_id);

                                current_user_db.child("name").setValue(name);
                                current_user_db.child("email").setValue(email);
                                current_user_db.child("contact").setValue(contact);
                                current_user_db.child("emergency").setValue(emgCon);
                                current_user_db.child("image").setValue("default");

                                progressDialog.dismiss();
                                Intent intent = new Intent(v.getContext(),Home.class);
                                startActivity(intent);



                            }
                            else{
                                Intent instant = new Intent(v.getContext(), MainActivity.class);
                                startActivity(instant);
                            }
                        }
                    });*/


                    Intent intent = new Intent(v.getContext(),HouseholdOwnerConfirmActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("name",name);
                    bundle.putString("email",email);
                    bundle.putString("password",password);
                    bundle.putString("image","url");

                    intent.putExtras(bundle);

                    startActivity(intent);





                }

                else{
                    Toast.makeText(v.getContext(),"Password does not match !!!",Toast.LENGTH_LONG);

                }




            }

        }




    }

}
