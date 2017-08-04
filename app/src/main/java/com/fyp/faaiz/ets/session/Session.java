package com.fyp.faaiz.ets.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Session {

    /* CONSTANTS */
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String CNIC = "cnic";
    public static final String PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String UUID = "uuid";

    // All Shared Preferences Keys
    public static final String IS_USER_LOGIN = "isLoggedIn";
    public static final String LOGIN_VAL = "LOGIN_VAL";
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;

    // Constructor
    public Session(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("EtsPref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    //Create login session
    public void createLoginSession(int id, String name, String email,String loginVal,String uuid,String first_name,String last_ame, String cnic, String phone) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
        // Storing name in pref
        editor.putInt(KEY_ID, id);
        // Storing name in pref
        editor.putString(KEY_NAME, name);

        editor.putString(FIRST_NAME, first_name);
        editor.putString(LAST_NAME, last_ame);
        editor.putString(CNIC, cnic);
        editor.putString(PHONE, phone);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        editor.putString(UUID, uuid);

        editor.putString(LOGIN_VAL, loginVal);

        editor.commit();
    }

    /* Login Check*/
    public boolean checkLogin(Intent i) {
        // Check login status
        if (!this.isUserLoggedIn()) {
            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(FIRST_NAME, pref.getString(FIRST_NAME, null));
        user.put(LAST_NAME, pref.getString(LAST_NAME, null));
        user.put(CNIC, pref.getString(CNIC, null));
        user.put(PHONE, pref.getString(PHONE, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        //uuid
        user.put(UUID, pref.getString(UUID, null));

        return user;
    }

    public HashMap<String, Integer> getId() {

        HashMap<String, Integer> userid = new HashMap<String, Integer>();

        userid.put(KEY_ID, pref.getInt(KEY_ID, 0));

        return userid;
    }

    public HashMap<String, String> getLoginVal() {

        HashMap<String, String> loginVal = new HashMap<String, String>();

        loginVal.put(LOGIN_VAL, pref.getString(LOGIN_VAL, null));

        return loginVal;
    }


    public void logoutUser() {

        editor.clear();

        editor.commit();
    }

    public boolean isUserLoggedIn() {

        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}