package com.mattikettu.pinkiponki;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;

import com.mattikettu.pinkiponki.ui.SlidingTabLayout;
import com.mattikettu.pinkiponki.ui.ImagePagerAdapter;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.ToastCreator;


import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    protected ToastCreator toastCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);

        setContentView(R.layout.base_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Fab, should actually be handled by the fragments.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toastCreator.showToastLong("Clicked button, idiot.");
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Sliding tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        viewPager.setAdapter(new ImagePagerAdapter(getFragmentManager()));
        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item_home) {
            toastCreator.showToastLong("nav_item_home");
        } else if (id == R.id.nav_item_profile) {
            toastCreator.showToastLong("nav_item_profile");
        } else if (id == R.id.nav_item_club) {
            toastCreator.showToastLong("nav_item_club");
        } else if (id == R.id.nav_item_settings) {
            toastCreator.showToastLong("nav_item_settings");
        } else if (id == R.id.nav_item_logout) {
            toastCreator.showToastLong("nav_item_logout");
        } else if (id == R.id.nav_item_about) {
            toastCreator.showToastLong("nav_item_about");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
