package com.spectradrive.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.spectradrive.Activities.AddChild.AddChildActivity;
import com.spectradrive.Activities.AddTrustedPerson.AddAPersonFragment;
import com.spectradrive.Activities.AddTrustedPerson.AddTrustedPersonActivity;
import com.spectradrive.Activities.AddTrustedPerson.AddTrustedPersonIntroActivity;
import com.spectradrive.Fragments.PersonalInfoFragment;
import com.spectradrive.Fragments.ProfileFragment;
import com.spectradrive.Fragments.RidesFragment;
import com.spectradrive.Helpers.LocalStorage;
import com.spectradrive.android.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainHomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView mTitle;

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

         drawer =  findViewById(R.id.drawer_layout);

                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationDrawerItemSelected(new RidesFragment());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            onNavigationDrawerItemSelected(new RidesFragment());
        } else if (id == R.id.nav_profile) {
            onNavigationDrawerItemSelected(new ProfileFragment());
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_view) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_rate_us) {

        }else if (id == R.id.nav_logout) {
            LocalStorage.clearAll();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
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

        if((LocalStorage.getStudent().getChild() == null || LocalStorage.getInt("numChild")> LocalStorage.getStudent().getChild().size())
                && !LocalStorage.isAddChildSkipped())
        {
            startActivity(new Intent(this, AddChildActivity.class));
        }else  if(LocalStorage.getStudent().getTrustedPersons() == null || LocalStorage.getStudent().getTrustedPersons().size() == 0){
            if(!LocalStorage.isTrustedPersonIntroShown()){
                LocalStorage.setTrustedPersonIntroShown(true);
                startActivity(new Intent(this, AddTrustedPersonIntroActivity.class));
            }else {
                if(!LocalStorage.isTrustedPersonIntroSkipped()){
                    startActivity(new Intent(this, AddTrustedPersonActivity.class));
                }
            }
        }

        //startActivity(new Intent(this, MapsActivity.class));
    }
}
