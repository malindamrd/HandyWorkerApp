package com.example.malindadeshapriya.handyworkerapp.Control;

/**
 * Created by malindadeshapriya on 4/26/18.
 */

public class RequestHistory {

    private String Owner,date,status,time,worker;
    private Double distance,lati,longi;

    public RequestHistory() {
    }

    public RequestHistory(String owner, String date, String status, String time, String worker, Double distance, Double lati, Double longi) {
        Owner = owner;
        this.date = date;
        this.status = status;
        this.time = time;
        this.worker = worker;
        this.distance = distance;
        this.lati = lati;
        this.longi = longi;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getLati() {
        return lati;
    }

    public void setLati(Double lati) {
        this.lati = lati;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }
}
