package com.example.malindadeshapriya.handyworkerapp.Control;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

/**
 * Created by malindadeshapriya on 2/16/18.
 */

public class GPSTracker implements LocationListener {

    Context context;
    Location location;



    public GPSTracker (Context c){
        context = c;


    }


    public Location getLocation(){

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context,"Permision not set",Toast.LENGTH_LONG).show();
            return null;
        }



        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnable){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,10,this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }
        else{
            Toast.makeText(context,"Please enable gps..!!",Toast.LENGTH_LONG).show();
        }

        return  null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(context,"Done",Toast.LENGTH_LONG).show();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
