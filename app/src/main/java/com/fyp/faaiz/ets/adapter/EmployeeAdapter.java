package com.fyp.faaiz.ets.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.faaiz.ets.R;
import com.fyp.faaiz.ets.model.Employee;
import com.fyp.faaiz.ets.tabs.employee.EmployeeDetail;

import java.util.ArrayList;
import java.util.List;

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

                Toast.makeText(myContext, mdata.get(position).getId() + "A", Toast.LENGTH_SHORT).show();
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
}
