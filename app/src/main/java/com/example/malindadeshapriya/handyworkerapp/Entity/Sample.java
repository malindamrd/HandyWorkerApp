package com.example.malindadeshapriya.handyworkerapp.Entity;

/**
 * Created by malindadeshapriya on 5/13/18.
 */

public class Sample {

    private String category;
    private String email;
    private String image;
    private double lati;
    private double longi;
    private String name;
    private String phone;
    private double rating;
    private String status;
    private String device_token;
    private String sampleID;
    private double distance;


    public Sample() {
    }

    public Sample(String category, String email, String image, double lati, double longi, String name, String phone, double rating, String status, String device_token) {
        this.category = category;
        this.email = email;
        this.image = image;
        this.lati = lati;
        this.longi = longi;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.status = status;
        this.device_token = device_token;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public void setSampleID(String sampleID){
        this.sampleID = sampleID;
    }

    public String getSampleID(){
        return sampleID;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }

    public double getDistance(){
        return distance;
    }
}
