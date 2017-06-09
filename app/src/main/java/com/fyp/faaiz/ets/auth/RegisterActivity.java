package com.fyp.faaiz.ets.auth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fyp.faaiz.ets.ApplicationState;
import com.fyp.faaiz.ets.MainActivity;
import com.fyp.faaiz.ets.R;
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
    public static final String PROFILE_IMAGE = "profile_image";
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
    ProgressBar progressBar;


    TextView counter_phone;
    TextView counter_cnic;

    AppCompatRadioButton owner_radio;
    AppCompatRadioButton user_radio;

    String table_radio = "ets_user_singup";
    public static String URL_SEND = "";

    private String email_filter_exp = "~#^|$%&*!+,':\";{}[]\\/()<?>";
    private String name_exp = "._-123456890@ ";
    private String phone_exp = "~#^|$%&*!,'.-_:\";{}[]\\/()<?> ";


    private InputFilter email_filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && email_filter_exp.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private InputFilter phone_filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && phone_exp.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private InputFilter name_filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && name_exp.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(ApplicationState.REMOTE_DATABASE_ACTIVE){
            URL_SEND = ApplicationState.REMOTE_BASE_URL + "/signup/user";
        }else{
            table_radio = "ets_user_signup";
            URL_SEND = ApplicationState.LOCAL_BASE_URL + "/ets/" + table_radio + ".php";
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar_signup);

        auth();
        init();

        owner_radio = (AppCompatRadioButton) findViewById(R.id.registerOwnerRadio);
        user_radio = (AppCompatRadioButton) findViewById(R.id.registerAgnetRadio);

        owner_radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if(ApplicationState.REMOTE_DATABASE_ACTIVE){
                        URL_SEND = ApplicationState.REMOTE_BASE_URL + "/signup/owner";
                    }else{
                        table_radio = "ets_owner_signup";
                        URL_SEND = ApplicationState.LOCAL_BASE_URL + "/ets/" + table_radio + ".php";
                    }

                    Toast.makeText(RegisterActivity.this, URL_SEND, Toast.LENGTH_SHORT).show();
                }
            }
        });

        user_radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(ApplicationState.REMOTE_DATABASE_ACTIVE){
                        URL_SEND = ApplicationState.REMOTE_BASE_URL + "/user/signup";
                    }else{
                        table_radio = "ets_user_signup";
                        URL_SEND = ApplicationState.LOCAL_BASE_URL + "/ets/" + table_radio + ".php";
                    }
                    Toast.makeText(RegisterActivity.this, URL_SEND, Toast.LENGTH_SHORT).show();
                }
            }
        });


        auth();
        init();

        email.setFilters(new InputFilter[]{email_filter});
        first_name.setFilters(new InputFilter[]{email_filter, name_filter});
        last_name.setFilters(new InputFilter[]{email_filter, name_filter});
        phone.setFilters(new InputFilter[]{phone_filter});

        cnic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (cnic.getText().length() >= 0) {
                    int count = 13;
                    counter_cnic.setText("Characters Left : " + (count - cnic.getText().length()));
                    counter_cnic.setTextColor(Color.GREEN);

                    if (cnic.getText().length() == 13) {
                        counter_cnic.setTextColor(Color.RED);
                    }

                    if (cnic.getText().length() > 13) {
                        counter_cnic.setTextColor(Color.RED);
                    }
                }
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (phone.getText().length() >= 0) {
                    int count = 13;
                    counter_phone.setText("Characters Left : " + (count - phone.getText().length()));
                    counter_phone.setTextColor(Color.GREEN);

                    if (phone.getText().length() == 13) {
                        counter_phone.setTextColor(Color.RED);
                    }

                    if (phone.getText().length() > 13) {
                        counter_phone.setTextColor(Color.RED);
                    }
                }
            }
        });

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
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URL_SEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("RESPONSE", response);
                Toast.makeText(RegisterActivity.this, response + URL_SEND, Toast.LENGTH_SHORT).show();
                if (response.contains("message")) {
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        signup.setBackground(getDrawable(R.drawable.botton_border));
                        signup.setEnabled(true);
                        signup.setText("Sign Up");
                    }else{
                        Drawable button = getResources().getDrawable(R.drawable.botton_border);
                        signup.setBackground(button);
                        signup.setEnabled(true);
                        signup.setText("Sign Up");
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    signup.setText("Sign Up");
                    Toast.makeText(RegisterActivity.this, "sorry some thing went wrong"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    signup.setBackground(getDrawable(R.drawable.botton_border));
                    signup.setEnabled(true);
                    signup.setText("Sign Up");
                }else{
                    Drawable button = getResources().getDrawable(R.drawable.botton_border);
                    signup.setBackground(button);
                    signup.setEnabled(true);
                    signup.setText("Sign Up");
                }
                progressBar.setVisibility(View.INVISIBLE);
                signup.setText("Sign Up");

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

        counter_phone = (TextView) findViewById(R.id.register_phone_number_count);
        counter_cnic = (TextView) findViewById(R.id.register_cnic_count);
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
                signup.setEnabled(false);
                signup.setBackgroundColor(Color.GRAY);
                signup.setText("");
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