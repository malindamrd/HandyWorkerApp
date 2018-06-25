package com.example.malindadeshapriya.handyworkerapp.Control;

/**
 * Created by malindadeshapriya on 2/27/18.
 */

public class DistanceCalculate {

    public  Double calculateDistance(Double worker_lati, Double worker_longi,Double lati,Double longi){

        double theta = longi - worker_longi;
        double dist = Math.sin(deg2rad(lati))
                * Math.sin(deg2rad(worker_lati))
                + Math.cos(deg2rad(lati))
                * Math.cos(deg2rad(worker_lati))
                * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public  double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
