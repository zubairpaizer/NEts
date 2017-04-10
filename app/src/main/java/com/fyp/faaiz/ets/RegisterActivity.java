package com.fyp.faaiz.ets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fyp.faaiz.ets.session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zubair on 2/17/17.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    /* Constant Vaiables */
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phone";
    public static final String CNIC = "cnic";
    public static final String PASSWORD = "password";

    TextView login_activity;
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText phone;
    EditText cnic;
    EditText password;
    EditText confirm_password;
    Button signup;
    Session _session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth();
        init();
        events();
    }

    protected void onResume() {
        super.onResume();
        if (_session.isUserLoggedIn()) {
            finish();
        }
    }

    private void auth() {
        _session = new Session(getApplicationContext());
        if (_session.isUserLoggedIn()) {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (_session.isUserLoggedIn()) {
            finish();
        }
    }


    public void events() {
        signup.setOnClickListener(this);
        login_activity.setOnClickListener(this);
    }

    private void registerRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.0.105/Ets/ets_user_signup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("RESPONSE", response);
                //Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(FIRST_NAME, first_name.getText().toString());
                params.put(LAST_NAME, last_name.getText().toString());
                params.put(EMAIL, email.getText().toString());
                params.put(PHONE_NUMBER, phone.getText().toString());
                params.put(CNIC, cnic.getText().toString());
                params.put(PASSWORD, password.getText().toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(request);
    }

    private boolean validate() {
        EditText[] editTexts = {first_name, last_name, email, phone, cnic, password, confirm_password};
        int[] max_range = {20, 20, 40, 13, 13, 40, 40};
        int[] min_range = {3, 3, 6, 11, 13, 6, 6};

        for (int i = 0; i < editTexts.length; i++) {
            if (editTexts[i].getText().length() > max_range[i]) {
                editTexts[i].setError("max range: " + max_range[i]);
                return true;
            }
        }

        for (int i = 0; i < editTexts.length; i++) {
            if (editTexts[i].getText().length() < min_range[i]) {
                editTexts[i].setError("min range: " + min_range[i]);
                return true;
            }
        }


        for (int i = 0; i < editTexts.length; i++) {
            if (TextUtils.isEmpty(editTexts[i].getText())) {
                editTexts[i].setError("field can't be blank");
                return true;
            }
        }


        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (TextUtils.isEmpty(email.getText())) {
            email.setError("email field can't be blank");
            return true;
        }

        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = p.matcher(email.getText());

        if (!matcher.matches()) {
            email.setError("invalid email address");
            return true;
        }

        if (!TextUtils.equals(password.getText(), confirm_password.getText())) {
            confirm_password.setError("password does not match");
            return true;
        }
        return false;
    }

    private void init() {

        signup = (Button) findViewById(R.id.register_signup_btn);
        login_activity = (TextView) findViewById(R.id.login_intent);
        first_name = (EditText) findViewById(R.id.register_first_name);
        last_name = (EditText) findViewById(R.id.register_last_name);
        email = (EditText) findViewById(R.id.register_email);
        phone = (EditText) findViewById(R.id.register_phone_number);
        cnic = (EditText) findViewById(R.id.register_cnic);
        password = (EditText) findViewById(R.id.register_password);
        confirm_password = (EditText) findViewById(R.id.register_confirm_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_signup_btn:
                if (validate()) return;
                registerRequest();
                break;
            case R.id.login_intent:
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
    }


}