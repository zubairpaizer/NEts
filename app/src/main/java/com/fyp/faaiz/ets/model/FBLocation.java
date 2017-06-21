package com.fyp.faaiz.ets.model;

/**
 * Created by zubairibrahim on 6/21/17.
 */

public class FBLocation {
    private String latitude;
    private String longitude;

    public FBLocation() {

    }

    public FBLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
