package com.fyp.faaiz.ets.model;

/**
 * Created by zubairibrahim on 6/21/17.
 */

public class FBRoot {
    private String user_id;
    private String email;
    private FBLocation location;

    public FBRoot(){

    }

    public FBRoot(String user_id, String email, FBLocation location) {
        this.user_id = user_id;
        this.email = email;
        this.location = location;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FBLocation getLocation() {
        return location;
    }

    public void setLocation(FBLocation location) {
        this.location = location;
    }
}
