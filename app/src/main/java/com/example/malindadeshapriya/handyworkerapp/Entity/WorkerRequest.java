package com.example.malindadeshapriya.handyworkerapp.Entity;

import android.widget.Toast;

import java.io.DataOutput;
import java.io.Serializable;

/**
 * Created by malindadeshapriya on 4/9/18.
 */

public class WorkerRequest implements Serializable {

    private String Owner;
    private double lati;
    private double longi;
    private double distance;
    private String status;
    private String worker;


    public WorkerRequest() {
    }

    public WorkerRequest(String owner, double lati, double longi, double distance, String status, String worker) {
        Owner = owner;
        this.lati = lati;
        this.longi = longi;
        this.distance = distance;
        this.status = status;
        this.worker = worker;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }
}
