package com.fyp.faaiz.ets.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.fyp.faaiz.ets.auth.LoginActivity;
import com.fyp.faaiz.ets.model.Employee;
import com.fyp.faaiz.ets.util.Parser;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fyp.faaiz.ets.R.id.login_email;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForgotPassword_Login.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForgotPassword_Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPassword_Login extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String EMAIL = "email";
    public static final String USER_ID = "user_id";
    Button btn_send;
    Button btn_cancel;
    ProgressBar progressBar;
    EditText email;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ForgotPassword_Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPassword_Login.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPassword_Login newInstance(String param1, String param2) {
        ForgotPassword_Login fragment = new ForgotPassword_Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootInflater = inflater.inflate(R.layout.fragment_forgot_password__login, container, false);
        email = (EditText) rootInflater.findViewById(R.id.email_forgot);
        btn_send = (Button) rootInflater.findViewById(R.id.forgot_send);
        btn_cancel = (Button) rootInflater.findViewById(R.id.forgot_cancel);
        progressBar = (ProgressBar) rootInflater.findViewById(R.id.forgot_send_progress);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(ForgotPassword_Login.this).commit();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRequest();
            }
        });

        return rootInflater;
    }


    private boolean validate() {
        /* email */
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

        if (email.getText().toString().length() > 40 || email.getText().toString().length() < 8) {
            email.setError("invalid email range");
            return true;
        }

        return false;
    }


    private void loginRequest() {
        if (validate()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn_send.setEnabled(true);
                btn_send.setText("Send");
                btn_send.setBackground(getActivity().getDrawable(R.drawable.botton_border));
            } else {
                Drawable button = getResources().getDrawable(R.drawable.botton_border);
                btn_send.setBackground(button);
            }
            btn_send.setEnabled(true);
            btn_send.setText("Send");
            return;
        }
        String URL_SEND = ApplicationState.REMOTE_BASE_URL + "/passwordrest/user/";
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URL_SEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "please check your email address", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_send.setBackground(getActivity().getDrawable(R.drawable.botton_border));
                    btn_send.setEnabled(true);
                    btn_send.setText("Send");
                } else {
                    Drawable button = getResources().getDrawable(R.drawable.botton_border);
                    btn_send.setBackground(button);
                    btn_send.setEnabled(true);
                    btn_send.setText("Send");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_send.setBackground(getActivity().getDrawable(R.drawable.botton_border));
                    btn_send.setEnabled(true);
                    btn_send.setText("Send");
                } else {
                    Drawable button = getResources().getDrawable(R.drawable.botton_border);
                    btn_send.setBackground(button);
                    btn_send.setEnabled(true);
                    btn_send.setText("Send");
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
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                volleyError.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(EMAIL, email.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(request);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
  /*      if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
