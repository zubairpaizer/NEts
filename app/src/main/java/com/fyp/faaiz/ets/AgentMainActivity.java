package com.fyp.faaiz.ets;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
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

public class AgentMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Session _session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_main);

        auth();
        init();

        // toolbar
        toolbar.setTitle("Company Name");

        String employeeName = _session.getUserDetails().get(Session.KEY_NAME);

        toolbar.setSubtitle(employeeName);
        setSupportActionBar(toolbar);

        startService(new Intent(AgentMainActivity.this,
                TrackerService.class));
        doBindService();
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
                    _session.logoutUser();
                    Intent i = new Intent(AgentMainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
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
