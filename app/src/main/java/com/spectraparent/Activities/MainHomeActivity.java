package com.spectraparent.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.spectraparent.Activities.AddChild.AddChildActivity;
import com.spectraparent.Activities.AddTrustedPerson.AddTrustedPersonActivity;
import com.spectraparent.Activities.AddTrustedPerson.AddTrustedPersonIntroActivity;
import com.spectraparent.Activities.HelpFAQ.FAQFragment;
import com.spectraparent.Fragments.ContactUsFragment;
import com.spectraparent.Fragments.ProfileFragment;
import com.spectraparent.Fragments.RateUsFragment;
import com.spectraparent.Fragments.RidesFragment;
import com.spectraparent.Fragments.SettingsFragment;
import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.android.R;

import butterknife.ButterKnife;

public class MainHomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView mTitle, tvNavHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText("Rides");

        getSupportActionBar().setTitle("");

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationDrawerItemSelected(new RidesFragment());
        View headerView = navigationView.getHeaderView(0);
        tvNavHeader = headerView.findViewById(R.id.tvNavHeader);

        tvNavHeader.setText(LocalStorage.getStudent().getFirstName() + " " + LocalStorage.getStudent().getLastName());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0)
                getFragmentManager().popBackStackImmediate();
            else super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rides) {
            mTitle.setText("Rides");
            onNavigationDrawerItemSelected(new RidesFragment());
        } else if (id == R.id.nav_profile) {
            onNavigationDrawerItemSelected(new ProfileFragment());
            mTitle.setText("Profile");
        } else if (id == R.id.nav_settings) {
            onNavigationDrawerItemSelected(new SettingsFragment());
            mTitle.setText("Settings");
        } else if (id == R.id.nav_view) {

        } else if (id == R.id.nav_contact) {
            onNavigationDrawerItemSelected(new ContactUsFragment());
            mTitle.setText("Contact Us");

        } else if (id == R.id.nav_invite) {
            startActivity(new Intent(this, InviteFriendsActivity.class));
            mTitle.setText("Invite");
        } else if (id == R.id.nav_rate_us) {
            onNavigationDrawerItemSelected(new RateUsFragment());
            mTitle.setText("Rate Us");

        } else if (id == R.id.nav_logout) {
            LocalStorage.clearAll();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_help) {
            onNavigationDrawerItemSelected(new FAQFragment());
            mTitle.setText("Help & FAQs");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onNavigationDrawerItemSelected(Fragment fragment) {
        // update the main content by replacing fragments
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager(); // For AppCompat use getSupportFragmentManager

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if ((LocalStorage.getStudent().getChild() == null || LocalStorage.getInt("numChild") > LocalStorage.getStudent().getChild().size())
                && !LocalStorage.isAddChildSkipped()) {
            startActivity(new Intent(this, AddChildActivity.class));
        } else if (LocalStorage.getStudent().getTrustedPersons() == null || LocalStorage.getStudent().getTrustedPersons().isEmpty()) {
                if (!LocalStorage.isTrustedPersonIntroShown()) {
                    LocalStorage.setTrustedPersonIntroShown(true);
                    startActivity(new Intent(this, AddTrustedPersonIntroActivity.class));
                } else {
                    if (!LocalStorage.isTrustedPersonIntroSkipped()) {
                        startActivity(new Intent(this, AddTrustedPersonActivity.class));
                    }
                }

            }
        //startActivity(new Intent(this, MapsActivity.class));
    }

    void getFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.v("getInstanceId failed", task.getException().toString());
                            return;
                        }
                        String token = task.getResult().getToken();
                        LocalStorage.storeString("firebase_token", token);
                        Log.v("getInstanceId success", token);

                    }
                });
    }
}
