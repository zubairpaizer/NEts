package com.fyp.faaiz.ets.auth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.transition.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.fyp.faaiz.ets.AgentMainActivity;
import com.fyp.faaiz.ets.ApplicationState;
import com.fyp.faaiz.ets.MainActivity;
import com.fyp.faaiz.ets.R;
import com.fyp.faaiz.ets.model.Employee;
import com.fyp.faaiz.ets.session.Session;
import com.fyp.faaiz.ets.util.Parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    TextView register_login;
    TextInputLayout textInputLayout;
    EditText login_password;
    EditText login_email;
    RadioButton login_agent;
    RadioButton login_owner;
    Button sigin_button;
    Session _session;
    ProgressBar progressBar;

    AppCompatRadioButton owner_radio;
    AppCompatRadioButton user_radio;
    String table_radio = "ets_user_login";
    public static String URL_SEND = "";

    private String email_filter = "~#^|$%&*!+,':\";{}[]\\/()<?>";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && email_filter.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ApplicationState.REMOTE_DATABASE_ACTIVE) {
            URL_SEND = ApplicationState.REMOTE_BASE_URL + "/login/user";
        } else {
            URL_SEND = ApplicationState.LOCAL_BASE_URL + "/Ets/" + table_radio + ".php";
        }


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        owner_radio = (AppCompatRadioButton) findViewById(R.id.loginOwnerRadio);
        user_radio = (AppCompatRadioButton) findViewById(R.id.loginAgentRadio);

        owner_radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (ApplicationState.REMOTE_DATABASE_ACTIVE) {
                        URL_SEND = ApplicationState.REMOTE_BASE_URL + "/login/owner";
                    } else {
                        table_radio = "ets_owner_login";
                        URL_SEND = ApplicationState.LOCAL_BASE_URL + "/ets/" + table_radio + ".php";
                    }
                    Toast.makeText(LoginActivity.this, URL_SEND, Toast.LENGTH_SHORT).show();
                }
            }
        });

        user_radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (ApplicationState.REMOTE_DATABASE_ACTIVE) {
                        URL_SEND = ApplicationState.REMOTE_BASE_URL + "/login/user";
                    } else {
                        table_radio = "ets_user_login";
                        URL_SEND = ApplicationState.LOCAL_BASE_URL + "/ets/" + table_radio + ".php";
                    }
                    Toast.makeText(LoginActivity.this, URL_SEND, Toast.LENGTH_SHORT).show();
                }
            }
        });


        // checking login
        init();
        auth();
        login_email.setFilters(new InputFilter[]{filter});
        init();
        events();
    }

    public void events() {
        login_password.setOnFocusChangeListener(this);
        register_login.setOnClickListener(this);
        sigin_button.setOnClickListener(this);
    }

    private void loginRequest() {
        if (validate()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sigin_button.setEnabled(true);
                sigin_button.setText("Sign In");
                sigin_button.setBackground(getDrawable(R.drawable.botton_border));
            } else {
                Drawable button = getResources().getDrawable(R.drawable.botton_border);
                sigin_button.setBackground(button);
            }
            sigin_button.setEnabled(true);
            sigin_button.setText("Sign In");
            return;
        }
        ;
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URL_SEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                if (response.contains("first_name")) {

                    List<Employee> parse = Parser.parse(response);

                    int local_id = parse.get(0).getId();

                    String local_full_name = parse.get(0).getFirst_name() + " " + parse.get(0).getLast_name();

                    String local_email = parse.get(0).getEmail();

                    Toast.makeText(LoginActivity.this, owner_radio.isChecked() + " / " + user_radio.isChecked() , Toast.LENGTH_SHORT).show();

                    if (owner_radio.isChecked()) {
                        _session.createLoginSession(local_id, local_full_name, local_email,"owner");
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else if(user_radio.isChecked()){
                        _session.createLoginSession(local_id, local_full_name, local_email,"agent");
                        Intent i = new Intent(LoginActivity.this, AgentMainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        sigin_button.setBackground(getDrawable(R.drawable.botton_border));
                        sigin_button.setEnabled(true);
                        sigin_button.setText("Sign In");
                    } else {
                        Drawable button = getResources().getDrawable(R.drawable.botton_border);
                        sigin_button.setBackground(button);
                        sigin_button.setEnabled(true);
                        sigin_button.setText("Sign In");
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    sigin_button.setText("Sign In");
                    Toast.makeText(LoginActivity.this, "username/password invalid", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sigin_button.setBackground(getDrawable(R.drawable.botton_border));
                    sigin_button.setEnabled(true);
                    sigin_button.setText("Sign In");
                } else {
                    Drawable button = getResources().getDrawable(R.drawable.botton_border);
                    sigin_button.setBackground(button);
                    sigin_button.setEnabled(true);
                    sigin_button.setText("Sign In");
                }
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
                        Log.d("Response", result);
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
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                volleyError.printStackTrace();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(EMAIL, login_email.getText().toString());
                params.put(PASSWORD, login_password.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(request);
    }

    private boolean validate() {
        /* email */
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (TextUtils.isEmpty(login_email.getText())) {
            login_email.setError("email field can't be blank");
            return true;
        }

        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = p.matcher(login_email.getText());


        if (!matcher.matches()) {
            login_email.setError("invalid email address");
            return true;
        }

        /* password */
        if (TextUtils.isEmpty(login_password.getText())) {
            login_password.setError("password field can't be blank");
            return true;
        }

        if (login_password.getText().toString().length() > 40 || login_password.getText().toString().length() < 6) {
            login_password.setError("invalid password range");
            return true;
        }

        if (login_email.getText().toString().length() > 40 || login_email.getText().toString().length() < 8) {
            login_email.setError("invalid email range");
            return true;
        }

        if (login_agent.isChecked()) {
            Toast.makeText(LoginActivity.this, login_agent.getText(), Toast.LENGTH_SHORT).show();
        }
        if (login_owner.isChecked()) {
            Toast.makeText(LoginActivity.this, login_owner.getText(), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void init() {
        textInputLayout = (TextInputLayout) findViewById(R.id.password_textinput);
        login_password = (EditText) findViewById(R.id.login_password);
        login_email = (EditText) findViewById(R.id.login_email);

        register_login = (TextView) findViewById(R.id.register_intent);

        login_agent = (RadioButton) findViewById(R.id.loginAgentRadio);
        login_owner = (RadioButton) findViewById(R.id.loginOwnerRadio);

        sigin_button = (Button) findViewById(R.id.signin_button);
    }

    private void auth() {
        _session = new Session(getApplicationContext());
        if (_session.isUserLoggedIn()) {
            if(_session.getLoginVal().get(Session.LOGIN_VAL).equals("owner")){
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }else if(_session.getLoginVal().get(Session.LOGIN_VAL).equals("agent")){
                Intent i = new Intent(LoginActivity.this, AgentMainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(this, "some thing went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (_session.isUserLoggedIn()) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_intent:
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.signin_button:
                sigin_button.setEnabled(false);
                sigin_button.setText("");
                sigin_button.setBackgroundColor(Color.GRAY);
                loginRequest();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.login_password:
                if (hasFocus) {
                    textInputLayout.setPasswordVisibilityToggleEnabled(true);
                }
                if (!hasFocus) {
                    textInputLayout.setPasswordVisibilityToggleEnabled(false);
                }
                break;
            default:
                break;
        }
    }
}
