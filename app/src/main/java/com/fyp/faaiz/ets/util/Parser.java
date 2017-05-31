package com.fyp.faaiz.ets.util;

import com.fyp.faaiz.ets.model.Employee;
import com.fyp.faaiz.ets.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.fyp.faaiz.ets.util.Helpers.contains;

/**
 * Created by zubair on 4/8/17.
 */

public class Parser {

    public static final String TAG = Parser.class.getSimpleName();

    public static ArrayList<Employee> parse(String response) {

        ArrayList<Employee> employees = new ArrayList<>();
        String first_name, last_name, email, profile_image, phone_number, cnic = "";
        int id = 0;

        try {
            JSONArray obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {

                Employee employee = new Employee();
                JSONObject jsonObject = obj.getJSONObject(i);

                if (contains(jsonObject, "id")) {
                    id = jsonObject.getInt("id");
                    employee.setId(id);
                }

                if (contains(jsonObject, "first_name")) {
                    first_name = jsonObject.getString("first_name");
                    employee.setFirst_name(first_name);
                }
                if (contains(jsonObject, "last_name")) {
                    last_name = jsonObject.getString("last_name");
                    employee.setLast_name(last_name);
                }

                if (contains(jsonObject, "email")) {
                    email = jsonObject.getString("email");
                    employee.setEmail(email);
                }

                if (contains(jsonObject, "profile_image")) {
                    profile_image = jsonObject.getString("profile_image");
                    employee.setProfile_image(profile_image);
                }

                if (contains(jsonObject, "phone_number")) {
                    phone_number = jsonObject.getString("phone_number");
                    employee.setPhone_number(phone_number);
                }

                if (contains(jsonObject, "cnic")) {
                    cnic = jsonObject.getString("cnic");
                    employee.setCnic(cnic);
                }

                employees.add(employee);
            }

            return employees;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Location> parseLocation(String response) {

        ArrayList<Location> locations = new ArrayList<>();

        int location_id = 0;
        int user_id = 0;
        String first_name = "";
        String last_name = "";
        String email = "";
        String phone_number = "";
        String profile_image = "";
        String cnic = "";
        String langitude = "";
        String latitude = "";
        String time_at = "";

        try {
            JSONArray obj = new JSONArray(response);
            for (int i = 0; i < obj.length(); i++) {

                Location location = new Location();
                JSONObject jsonObject = obj.getJSONObject(i);

                if (contains(jsonObject, "location_id")) {
                    location_id = jsonObject.getInt("location_id");
                    location.setLocation_id(location_id);
                }

                if (contains(jsonObject, "user_id")) {
                    user_id = jsonObject.getInt("user_id");
                    location.setUser_id(user_id);
                }

                if (contains(jsonObject, "first_name")) {
                    first_name = jsonObject.getString("first_name");
                    location.setFirst_name(first_name);
                }
                if (contains(jsonObject, "last_name")) {
                    last_name = jsonObject.getString("last_name");
                    location.setLast_name(last_name);
                }

                if (contains(jsonObject, "email")) {
                    email = jsonObject.getString("email");
                    location.setEmail(email);
                }

                if (contains(jsonObject, "profile_image")) {
                    profile_image = jsonObject.getString("profile_image");
                    location.setProfile_image(profile_image);
                }

                if (contains(jsonObject, "phone_number")) {
                    phone_number = jsonObject.getString("phone_number");
                    location.setPhone_number(phone_number);
                }

                if (contains(jsonObject, "cnic")) {
                    cnic = jsonObject.getString("cnic");
                    location.setCnic(cnic);
                }

                if (contains(jsonObject, "langitude")) {
                    langitude = jsonObject.getString("langitude");
                    location.setLangitude(langitude);
                }

                if (contains(jsonObject, "latitude")) {
                    latitude = jsonObject.getString("latitude");
                    location.setLatitude(latitude);
                }

                if (contains(jsonObject, "time_at")) {
                    time_at = jsonObject.getString("time_at");
                    location.setTime_at(time_at);
                }

                locations.add(location);
            }

            return locations;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
