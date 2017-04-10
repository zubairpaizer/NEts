package com.fyp.faaiz.ets.utils;

import android.util.Log;

import com.fyp.faaiz.ets.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.fyp.faaiz.ets.utils.Helpers.contains;

/**
 * Created by zubair on 4/8/17.
 */

public class Parser {

    public static final String TAG = Parser.class.getSimpleName();

    public static List<Employee> parse(String response) {

        String first_name, last_name, email, profile_image, phone_number, cnic = "";
        int id = 0;

        try {

            JSONArray obj = new JSONArray(response);
            List<Employee> employees = new ArrayList<>();
            Employee employee = new Employee();

            for (int i = 0; i < obj.length(); i++) {

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
                Log.d(TAG, "Employee Parse: " + employee);
            }

            return employees;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
