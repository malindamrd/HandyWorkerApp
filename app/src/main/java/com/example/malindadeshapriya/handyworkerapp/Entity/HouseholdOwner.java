package com.example.malindadeshapriya.handyworkerapp.Entity;

/**
 * Created by malindadeshapriya on 5/3/18.
 */

public class HouseholdOwner {
    String email;
    String image;
    double lat;
    double longi;
    String name;
    String phone;

    public HouseholdOwner() {
    }

    public HouseholdOwner(String email, String image, double lat, double longi, String name, String phone) {
        this.email = email;
        this.image = image;
        this.lat = lat;
        this.longi = longi;
        this.name = name;
        this.phone = phone;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
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
}
