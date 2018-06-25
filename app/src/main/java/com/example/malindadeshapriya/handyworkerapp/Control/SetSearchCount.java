package com.example.malindadeshapriya.handyworkerapp.Control;

import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by malindadeshapriya on 5/8/18.
 */

public class SetSearchCount {

    String searchKey;
    int count;
    String isNull = null;
    ValueEventListener listener;
    Query query;

    public SetSearchCount(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setCount(){
        query = FirebaseDatabase.getInstance().getReference("SearchCount").child(searchKey);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SearchCount").child(searchKey);

        listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    count = Integer.parseInt(dataSnapshot.child("count").getValue().toString());
                    count++;
                    databaseReference.child("count").setValue(count);
                    query.removeEventListener(listener);
                }
                else{
                    count=1;
                    databaseReference.child("count").setValue(count);
                    query.removeEventListener(listener);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
