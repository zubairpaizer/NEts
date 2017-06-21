package com.fyp.faaiz.ets.tabs.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.fyp.faaiz.ets.R;
import com.fyp.faaiz.ets.model.FBRoot;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeTabsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeTabsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeTabsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HOMETABS";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View _rootView;
    MapView mMapView;

    public static final int ERROR_DIALOG_REQUEST = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;
    private LocationListener mLocationListener;
    private DatabaseReference mDatabase;
    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;

    public HomeTabsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                googleMap.clear();
                try {
                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                        //FBRoot fbRoot = locationSnapshot.getValue(FBRoot.class);
                        String email = locationSnapshot.child("email").getValue().toString();
                        String user_id = locationSnapshot.child("user_id").getValue().toString();
                        double latitude = Double.valueOf(locationSnapshot.child("location").child("latitude").getValue().toString());
                        double longitude = Double.valueOf(locationSnapshot.child("location").child("longitude").getValue().toString());
                        Toast.makeText(getActivity(), latitude + "\n" + longitude + "", Toast.LENGTH_SHORT).show();

                        LatLng userLocation = new LatLng(latitude, longitude);
                        googleMap.addMarker(new MarkerOptions().position(userLocation).title(email).snippet(user_id));
                        //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
                        //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getActivity(), "Failed to load locations.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        mPostReference.addValueEventListener(listener);
        mPostListener = listener;
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
    public static HomeTabsFragment newInstance(String param1, String param2) {
        HomeTabsFragment fragment = new HomeTabsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://nets-8cb47.firebaseio.com/employees");
        mPostReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://nets-8cb47.firebaseio.com/employees");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }

    private void checkPermission() {
        boolean permissionGrantedFine = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean permissionGrantedCoarse = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;


        if (permissionGrantedFine) {
            //Toast.makeText(getActivity(), "permissionGrantedFine", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getActivity(), "Not permissionGrantedFine", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        if (permissionGrantedCoarse) {
            //Toast.makeText(getActivity(), "permissionGrantedCoarse", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getActivity(), "Not permissionGrantedCoarse", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), grantResults[0], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (serviceOK()) {

            _rootView = inflater.inflate(R.layout.home_map, container, false);
            mMapView = (MapView) _rootView.findViewById(R.id.map);

            mMapView.onCreate(savedInstanceState);
            mMapView.onResume(); // needed to get the map to display immediately


            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Return", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        googleMap = mMap;
                        LatLng sydney = new LatLng(24.8615, 67.0099);
                        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(15).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        googleMap.setMyLocationEnabled(true);
                    }

                }
            });

        } else {
            _rootView = inflater.inflate(R.layout.home_tabs_fragment, container, false);
        }
        return _rootView;
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
/*
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
*/
        }
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

    /*
    public void getFuseLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getActivity(), "Location permission required", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (currentLocation == null) {
                //Toast.makeText(getActivity(), "Location Not Found", Toast.LENGTH_SHORT).show();
            } else {
                LatLng latlng = new LatLng(2444, 2458);
                Toast.makeText(getActivity(), latlng.toString(), Toast.LENGTH_SHORT).show();
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, 15);
                googleMap.animateCamera(update);
            }
        }
    }*/


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

    public void setLocations(double lat, double lng, int zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoom).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

/*
    */
/* GOOGLE API CLIENT *//*

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Toast.makeText(getActivity(), "Ready to Map", Toast.LENGTH_SHORT).show();
        getFuseLocation();
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getActivity(), location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                setLocations(location.getLatitude(), location.getLongitude(), 15);
            }
        };

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(1000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, mLocationListener);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

*/

}
