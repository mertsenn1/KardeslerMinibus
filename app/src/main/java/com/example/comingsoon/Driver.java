package com.example.comingsoon;

import com.google.android.gms.maps.model.LatLng;

/**
 * This class is a simple driver class. It has also own properties and functions to obtain location and route.
 */
public class Driver {

    // Properties
    private String name;
    private String email;
    private LatLng location;
    private int route;
    private float bearing;

    // Constructors
    public Driver() {

    }

    // Methods
    public Driver( String name, String email) {
        this.name = name;
        this.email = email;
        this.route = 0;
        this.location = new LatLng( 0, 0);
        this.bearing = 0;
    }

    public String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute( int route) {
        this.route = route;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation( LatLng location) {
        this.location = location;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing( LatLng location) {
        this.bearing = bearing;
    }
}