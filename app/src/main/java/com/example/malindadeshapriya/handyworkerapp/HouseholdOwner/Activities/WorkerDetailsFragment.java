package com.example.malindadeshapriya.handyworkerapp.HouseholdOwner.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkerDetailsFragment extends Fragment {


    private String na;

    View v;
    public WorkerDetailsFragment() {
        // Required empty public constructor
    }

    public void setName(String name){
        na = name;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v =  inflater.inflate(R.layout.fragment_worker_details, container, false);
        Toast.makeText(v.getContext(),na,Toast.LENGTH_LONG).show();
        return v;
    }

}
