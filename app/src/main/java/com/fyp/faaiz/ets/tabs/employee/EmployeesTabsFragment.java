package com.fyp.faaiz.ets.tabs.employee;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.fyp.faaiz.ets.LoginActivity;
import com.fyp.faaiz.ets.MainActivity;
import com.fyp.faaiz.ets.NavigationDrawerItem;
import com.fyp.faaiz.ets.R;
import com.fyp.faaiz.ets.adapter.EmployeeAdapter;
import com.fyp.faaiz.ets.adapter.NavigationDrawerAdapter;
import com.fyp.faaiz.ets.model.Employee;
import com.fyp.faaiz.ets.utils.Parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zubair on 4/3/17.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployeesTabsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmployeesTabsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class EmployeesTabsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View _rootView;
    public SwipeRefreshLayout swipeRefresher;
    ArrayList<Employee> employees;
    public EmployeesTabsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeesTabsFragment newInstance(String param1, String param2) {
        EmployeesTabsFragment fragment = new EmployeesTabsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employees = new ArrayList<Employee>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.employee_tab_fragment, container, false);

        swipeRefresher = (SwipeRefreshLayout) _rootView.findViewById(R.id.swipe_refresher);

        request();

        swipeRefresher.setOnRefreshListener(this);

        swipeRefresher.post(new Runnable() {
                                @Override
                                public void run() {
                                    request();
                                }
                            }
        );

        return _rootView;
    }

    private void request() {
        swipeRefresher.setRefreshing(true);
        StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.0.107/Ets/list_all_employees.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("first_name")) {

                    RecyclerView recyclerView = (RecyclerView) _rootView.findViewById(R.id.employee_list);

                    employees = Parser.parse(response);

                    EmployeeAdapter adapter = new EmployeeAdapter(getActivity(), employees);

                    recyclerView.setAdapter(adapter);

                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    swipeRefresher.setRefreshing(false);

                } else {
                    Toast.makeText(getActivity(), "sorry some thing went wrong", Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
    /*        throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
    */
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        request();
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

