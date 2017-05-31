package com.fyp.faaiz.ets.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.fyp.faaiz.ets.ApplicationState;
import com.fyp.faaiz.ets.EmployeeLocationOnMap;
import com.fyp.faaiz.ets.R;
import com.fyp.faaiz.ets.model.Employee;
import com.fyp.faaiz.ets.model.Location;
import com.fyp.faaiz.ets.tabs.employee.EmployeeDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by zubairibrahim on 5/29/17.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MH> {

    Context myContext;
    ArrayList<Location> mdata;
    FragmentManager fragmentManager;

    public LocationAdapter(Context context, ArrayList<Location> data, FragmentManager fragmentManager1) {
        this.myContext = context;
        this.mdata = data;
        this.fragmentManager = fragmentManager1;
    }

    @Override
    public MH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_location_item, parent, false);
        MH mh = new MH(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(MH holder, final int position) {
        String sqlTime = mdata.get(position).getTime_at();
        String date = sqlTime.substring(0, 10).replaceAll("-", "/");
        String time = sqlTime.substring(10, 19);
        final String td = "Time : " + time + "\nDate : " + date;
        holder.title.setText(td);
        holder.desc.setText(mdata.get(position).getLatitude() + " / " + mdata.get(position).getLangitude());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myContext);
                alertDialogBuilder.setTitle("Do you want to delete?");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                request(mdata.get(position).getUser_id(), position);
                                Toast.makeText(myContext, mdata.get(position).getUser_id() + "A", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeLocationOnMap m = EmployeeLocationOnMap.newInstance(mdata.get(position).getLatitude(), mdata.get(position).getLangitude(),td,mdata.get(position).getFullName());
                m.show(fragmentManager, "FRAGMENT_DIALOG");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MH extends RecyclerView.ViewHolder {

        TextView title;
        TextView desc;
        ImageView detail;
        ImageView delete;

        public MH(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.location_name);
            this.desc = (TextView) itemView.findViewById(R.id.location_desc);
            this.detail = (ImageView) itemView.findViewById(R.id.location_detail);
            this.delete = (ImageView) itemView.findViewById(R.id.delete_location);
        }
    }

    private void request(int id, final int index) {
        final LocationAdapter t = this;
        String URL_SEND = "";

        if (ApplicationState.REMOTE_DATABASE_ACTIVE) {
            URL_SEND = ApplicationState.REMOTE_BASE_URL + "/location/" + id;
        } else {
            URL_SEND = ApplicationState.LOCAL_BASE_URL + "/ets/location_delete.php?id=" + id;
        }

        Toast.makeText(myContext, URL_SEND, Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(Request.Method.DELETE, URL_SEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mdata.remove(index);
                t.notifyDataSetChanged();
                Toast.makeText(myContext, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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
        RequestQueue queue = Volley.newRequestQueue(myContext);
        queue.add(request);
    }
}
