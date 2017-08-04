package com.fyp.faaiz.ets;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.fyp.faaiz.ets.auth.LoginActivity;
import com.fyp.faaiz.ets.drawer.NavigationDrawerFragment;
import com.fyp.faaiz.ets.service.TrackerService;
import com.fyp.faaiz.ets.session.Session;
import com.fyp.faaiz.ets.tabs.TabsFragment;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    DrawerLayout drawerLayout;
    Session _session;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth();
        init();

        // toolbar
        toolbar.setTitle("ETS");
        toolbar.setSubtitle("Sub Title");
        setSupportActionBar(toolbar);

        // viewpager
        TabsFragment tabAdapter = new TabsFragment(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // drawer
        setUpDrawer();

/*
        startService(new Intent(MainActivity.this,
                TrackerService.class));
        doBindService();
*/
    }
    /*

        private void doBindService() {
            bindService(new Intent(this, TrackerService.class), mConnection,
                    Context.BIND_AUTO_CREATE);
        }

        private ServiceConnection mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name,   IBinder service) {
                Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            }
        };

    */
        @Override
    protected void onResume() {
        super.onResume();
        if (!_session.isUserLoggedIn()) {
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!_session.isUserLoggedIn()) {
            finish();
        }
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

    }

    private void auth() {
        _session = new Session(getApplicationContext());
        if (!_session.isUserLoggedIn()) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }

    public void setUpDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_fragment);
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUpDrawer(R.id.nav_drawer_fragment, mDrawer, toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.owner, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                if (_session.isUserLoggedIn()) {
                    _session.logoutUser();
                    if(TrackerService.isRunning()){
                        TrackerService t = new TrackerService();
                        stopService(new Intent(MainActivity.this,TrackerService.class));
                        //t.
                    }
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
/*            case R.id
                    .setting:
                Intent settingIntent = new Intent(this, SettingPref.class);
                startActivity(settingIntent);
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();*/
        }
        return super.onOptionsItemSelected(item);
    }
}
