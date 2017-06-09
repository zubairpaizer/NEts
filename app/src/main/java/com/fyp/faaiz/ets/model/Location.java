package com.fyp.faaiz.ets.model;

/**
 * Created by zubair on 4/3/17.
 */

public class Location {

    public int location_id;
    public String uuid;
    public int user_id;
    public String first_name;
    public String last_name;
    public String email;
    public String phone_number;
    public String profile_image;
    public String cnic;
    public String langitude;
    public String latitude;
    public String time_at;

    public Location() {

    }

    public Location(int location_id, String uuid, int user_id, String first_name, String last_name, String email, String phone_number, String profile_image, String cnic, String langitude, String latitude, String time_at) {
        this.location_id = location_id;
        this.uuid = uuid;
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.profile_image = profile_image;
        this.cnic = cnic;
        this.langitude = langitude;
        this.latitude = latitude;
        this.time_at = time_at;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getLangitude() {
        return langitude;
    }

    public void setLangitude(String langitude) {
        this.langitude = langitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTime_at() {
        return time_at;
    }

    public void setTime_at(String time_at) {
        this.time_at = time_at;
    }

    public String getFullName(){
        return this.first_name + " " + this.last_name;
    }

}
