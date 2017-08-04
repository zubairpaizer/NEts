package com.fyp.faaiz.ets;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fyp.faaiz.ets.adapter.LocationAdapter;
import com.fyp.faaiz.ets.auth.LoginActivity;
import com.fyp.faaiz.ets.model.Location;
import com.fyp.faaiz.ets.util.Parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class EmployeeLocationDetails extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefresher;
    RecyclerView r;
    ArrayList<Location> locations;
    Toolbar toolbar;
    int empId = 0;
    LocationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_recycleview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        int empID = Integer.parseInt(getIntent().getExtras().get("empId").toString());
        empId = empID;
        final String name = getIntent().getExtras().get("name").toString();
        final String empEmail = getIntent().getExtras().get("emailId").toString();


        toolbar.setTitle(name + " Locations");
        toolbar.setSubtitle(empEmail);

        setSupportActionBar(toolbar);
        r = (RecyclerView) findViewById(R.id.location_r_r);
        swipeRefresher = (SwipeRefreshLayout) findViewById(R.id.location_swipe);
        swipeRefresher.setOnRefreshListener(this);


        Toast.makeText(this, empID + " ", Toast.LENGTH_SHORT).show();

        request(empID, "", true);
    }

    private void request(int user_id, String Url, boolean extURL) {
        String URL_SEND = "";
        if (extURL) {
            if (ApplicationState.REMOTE_DATABASE_ACTIVE) {
                URL_SEND = ApplicationState.REMOTE_BASE_URL + "/locations/user/" + user_id;
            } else {
                URL_SEND = ApplicationState.LOCAL_BASE_URL + "/ets/list_all_employees.php";
            }
        } else {
            URL_SEND = Url;
            Log.d("PARSE",URL_SEND);
        }
        swipeRefresher.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.GET, URL_SEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("first_name")) {
                    locations = Parser.parseLocation(response);
                    adapter = new LocationAdapter(getApplicationContext(), locations, getSupportFragmentManager());
                    r.setAdapter(adapter);
                    r.setHasFixedSize(true);
                    r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter.notifyDataSetChanged();
                    swipeRefresher.setRefreshing(false);
                } else {
                    Toast.makeText(getApplicationContext(), "sorry some thing went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeRefresher.setRefreshing(false);
                NetworkResponse networkResponse = volleyError.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (volleyError.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (volleyError.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404)
                            errorMessage = "Resource not found";
                        else if (networkResponse.statusCode == 401)
                            errorMessage = message + " Please login again";
                        else if (networkResponse.statusCode == 400)
                            errorMessage = message + " Check your inputs";
                        else if (networkResponse.statusCode == 500)
                            errorMessage = message + " Something is getting wrong";

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                volleyError.printStackTrace();

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onRefresh() {
        request(empId, "", true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.date_picker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.date_picker) {
            showDatePicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year,
                              int monthOfYear,
                              int dayOfMonth) {

            String sqldate = String.valueOf(year) + "-0" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);
            //https://nets.herokuapp.com/locations/date/38/2017-05-27

            String URL = "https://nets.herokuapp.com/locations/date/" + empId + "/" + sqldate;

            toolbar.setSubtitle("Selected Date : " + sqldate);

            Toast.makeText(EmployeeLocationDetails.this, sqldate, Toast.LENGTH_SHORT).show();

            locations = new ArrayList<>();

            adapter = new LocationAdapter(getApplicationContext(), locations , getSupportFragmentManager());

            r.swapAdapter(adapter,true);

            adapter.notifyDataSetChanged();

            request(empId,URL,false);

        }
    };
}
