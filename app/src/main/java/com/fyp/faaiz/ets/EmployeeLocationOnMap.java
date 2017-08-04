package com.fyp.faaiz.ets;

import android.*;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fyp.faaiz.ets.tabs.home.HomeTabsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.fyp.faaiz.ets.tabs.home.HomeTabsFragment.ERROR_DIALOG_REQUEST;

public class EmployeeLocationOnMap extends DialogFragment {

    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    public static final String TIME_AT = "TIME_AT";
    public static final String EMPLOYEE_NAME = "EMPLOYEE_NAME";


    View rootView;
    Button btnDone;
    MapView mapView;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;

    public EmployeeLocationOnMap() {
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
    public static EmployeeLocationOnMap newInstance(String param1, String param2,String param3, String param4) {
        EmployeeLocationOnMap fragment = new EmployeeLocationOnMap();
        Bundle args = new Bundle();
        args.putString(LATITUDE, param1);
        args.putString(LONGITUDE, param2);
        args.putString(TIME_AT, param3);
        args.putString(EMPLOYEE_NAME, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_employee_location_on_map, container);

        if(serviceOK()) {

            mapView = (MapView) rootView.findViewById(R.id.map_employee);
            mapView.onCreate(savedInstanceState);
            mapView.onResume(); // needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Return", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Double lat = Double.valueOf(getArguments().get(LATITUDE).toString());
                        Double lng =  Double.valueOf(getArguments().get(LONGITUDE).toString());
                        googleMap = mMap;
                        LatLng trackedLocation = new LatLng(lat,lng);
                        googleMap.addMarker(new MarkerOptions().position(trackedLocation).title(getArguments().get(EMPLOYEE_NAME).toString()).snippet(getArguments().get(TIME_AT).toString()));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(trackedLocation).zoom(16).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        //googleMap.setMyLocationEnabled(true);
                    }

                }
            });
        }else{
            Toast.makeText(getActivity(), "google api Client is not working", Toast.LENGTH_SHORT).show();
        }

        btnDone = (Button) rootView.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    public boolean serviceOK() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, getActivity(), ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "can't connect to google map service", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
