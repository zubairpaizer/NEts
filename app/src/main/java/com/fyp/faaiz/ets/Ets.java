package com.fyp.faaiz.ets;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by zubairibrahim on 6/6/17.
 */

public class Ets extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
