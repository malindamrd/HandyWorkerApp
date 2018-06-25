package com.example.malindadeshapriya.handyworkerapp.Control;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import static java.lang.Math.toIntExact;

/**
 * Created by malindadeshapriya on 5/9/18.
 */

public class GetSearchCount {


    int catCount = 0;

    String count [][];
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SearchCount");
    Query query = FirebaseDatabase.getInstance().getReference("SearchCount");
    ValueEventListener listener;

    public GetSearchCount() {
    }

    public String[][] counts(){
        int j=0;
        listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                catCount = (int) dataSnapshot.getChildrenCount();
                count= new String[catCount][2];
                int i=0;
                for(DataSnapshot c : dataSnapshot.getChildren()){
                    count[i][0] = c.getKey().toString();
                    count[i][1] = c.child("count").getValue().toString();
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return count ;

    }

}
