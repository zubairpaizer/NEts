package com.fyp.faaiz.ets;

import android.*;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fyp.faaiz.ets.auth.LoginActivity;
import com.fyp.faaiz.ets.service.TrackerService;
import com.fyp.faaiz.ets.session.Session;
import com.fyp.faaiz.ets.tabs.TabsFragment;
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

public class AgentMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Session _session;
    MapView mMapView;
    public static final int ERROR_DIALOG_REQUEST = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        auth();
        init();
        mMapView = (MapView) findViewById(R.id.map_agent);
        checkPermission();
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately


        try {
            MapsInitializer.initialize(getApplicationContext().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Return", Toast.LENGTH_SHORT).show();
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

        // toolbar
        toolbar.setTitle("Ets");

        String employeeName = _session.getUserDetails().get(Session.KEY_NAME);

        toolbar.setSubtitle(employeeName);
        setSupportActionBar(toolbar);

        startService(new Intent(AgentMainActivity.this,
                TrackerService.class));
        doBindService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, grantResults[0], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkPermission() {
        boolean permissionGrantedFine = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean permissionGrantedCoarse = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permissionGrantedFine) {
            //Toast.makeText(getActivity(), "permissionGrantedFine", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getActivity(), "Not permissionGrantedFine", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        if (permissionGrantedCoarse) {
            //Toast.makeText(getActivity(), "permissionGrantedCoarse", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getActivity(), "Not permissionGrantedCoarse", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!_session.isUserLoggedIn()) {
            finish();
        }
    }

    private void auth() {
        _session = new Session(getApplicationContext());
        if (!_session.isUserLoggedIn()) {
            Intent i = new Intent(AgentMainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                if (_session.isUserLoggedIn()) {
                    if(TrackerService.isRunning()){
                        TrackerService t = new TrackerService();
                        stopService(new Intent(AgentMainActivity.this,TrackerService.class));
                        //t.
                    }
                    _session.logoutUser();
                    Intent i = new Intent(AgentMainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            case R.id.profile:
                if (_session.isUserLoggedIn()) {
                    Intent i = new Intent(AgentMainActivity.this, ChangeUserDetails.class);
                    startActivity(i);
                }

        }
        return super.onOptionsItemSelected(item);
    }


    private void doBindService() {
        bindService(new Intent(this, TrackerService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(AgentMainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(AgentMainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };


}
