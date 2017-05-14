package com.fyp.faaiz.ets.tabs.employee;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.faaiz.ets.R;

public class EmployeeDetail extends AppCompatActivity {

    TextView mobile_tv;
    TextView email_tv;
    TextView address_tv;
    ImageView iv_call;
    ImageView iv_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_detail);

        mobile_tv = (TextView) findViewById(R.id.employee_number);
        email_tv = (TextView) findViewById(R.id.employee_email);
        address_tv = (TextView) findViewById(R.id.employee_address);
        iv_call = (ImageView) findViewById(R.id.user_phone);
        iv_email = (ImageView) findViewById(R.id.user_email);

        String empID = getIntent().getExtras().get("empId").toString();
        final String name = getIntent().getExtras().get("name").toString();
        final String empEmail = getIntent().getExtras().get("emailId").toString();
        final String address = getIntent().getExtras().get("address").toString();
        final String mobile = getIntent().getExtras().get("mobile").toString();

        address_tv.setText(address);
        mobile_tv.setText(mobile);
        email_tv.setText(empEmail);

        final String newNum = mobile.replace("+", "00");
        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EmployeeDetail.this, "Call", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + newNum));
                startActivity(intent);
            }
        });

        iv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{empEmail});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT, "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
