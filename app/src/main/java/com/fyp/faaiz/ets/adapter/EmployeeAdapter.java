package com.fyp.faaiz.ets.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.fyp.faaiz.ets.R;
import com.fyp.faaiz.ets.model.Employee;
import com.fyp.faaiz.ets.tabs.employee.EmployeeDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zubairibrahim on 4/15/17.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MVH> {

    Context myContext;
    ArrayList<Employee> mdata;

    public EmployeeAdapter(Context context, ArrayList<Employee> data) {
        this.myContext = context;
        this.mdata = data;
    }

    @Override
    public MVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_item, parent, false);
        MVH mvh = new MVH(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MVH holder, final int position) {
        holder.title.setText(mdata.get(position).getFullName());
        holder.desc.setText(mdata.get(position).getEmail());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myContext);
                alertDialogBuilder.setTitle("Do you want to delete?");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                request(mdata.get(position).getId(),position);
                                Toast.makeText(myContext, mdata.get(position).getId() + "A", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(myContext, EmployeeDetail.class);

                int newId = (int) mdata.get(position).getId();
                String fname = mdata.get(position).getFirst_name();
                String lname = mdata.get(position).getLast_name();
                String emailId = mdata.get(position).getEmail();
                String mobile = mdata.get(position).getPhone_number();
                String address = "Address .. .. ..";

                String full_name = fname + " " + lname;
                i.putExtra("empId", newId);
                i.putExtra("name", full_name);
                i.putExtra("emailId", emailId);
                i.putExtra("address", address);
                i.putExtra("mobile", mobile);

                myContext.startActivity(i);
                Toast.makeText(myContext, mdata.get(position).getId() + mdata.get(position).getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class MVH extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        ImageView imageView;
        ImageView detail;
        ImageView delete;

        public MVH(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.employee_name);
            this.desc = (TextView) itemView.findViewById(R.id.employee_desc);
            this.imageView = (ImageView) itemView.findViewById(R.id.employee_image);
            this.delete = (ImageView) itemView.findViewById(R.id.delete_employee);
            this.detail = (ImageView) itemView.findViewById(R.id.detail_employee);
        }
    }

    private void request(int id, final int index) {
        final EmployeeAdapter t = this;
        String URL_SEND = "";
        if(ApplicationState.REMOTE_DATABASE_ACTIVE){
            URL_SEND = ApplicationState.REMOTE_BASE_URL + "/employee/" + id;
        }else{
            URL_SEND = ApplicationState.LOCAL_BASE_URL + "/ets/list_all_employees.php?id=" + id;
        }

        Toast.makeText(myContext,URL_SEND, Toast.LENGTH_SHORT).show();
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
