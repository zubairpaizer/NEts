package com.fyp.faaiz.ets.model;

/**
 * Created by zubair on 4/3/17.
 */

public class Employee {

    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private String profile_image;
    private String cnic;

    private String password;
    private String updated_at;
    private String created_at;

    public Employee() {
    }

    public Employee(int id, String first_name, String last_name, String email, String phone_number, String profile_image, String cnic) {

        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.profile_image = profile_image;
        this.cnic = cnic;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public String getFullName(){
        return this.first_name + " " + this.last_name;
    }

}
