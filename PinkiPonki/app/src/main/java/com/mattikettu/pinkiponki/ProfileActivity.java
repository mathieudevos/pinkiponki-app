package com.mattikettu.pinkiponki;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattikettu.pinkiponki.networkapi.CurrentUser;
import com.mattikettu.pinkiponki.networkapi.NetworkLogic;
import com.mattikettu.pinkiponki.util.Constants;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.SharedPreferenceManager;
import com.mattikettu.pinkiponki.util.ToastCreator;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "PROFILEACTIVYT";

    @Inject
    protected ToastCreator toastCreator;

    @Inject
    protected SharedPreferenceManager sharedPreferenceManager;

    @Inject
    protected CurrentUser currentUser;


    @BindView(R.id.profile_username)
    TextView profile_username;

    @BindView(R.id.profile_rating_big)
    TextView profile_rating_big;

    @BindView(R.id.profile_rating)
    TextView profile_rating;

    @BindView(R.id.profile_maxrating)
    TextView profile_maxrating;

    @BindView(R.id.profile_firstname)
    TextView profile_firstname;

    @BindView(R.id.profile_lastname)
    TextView profile_lastname;

    @BindView(R.id.profile_about)
    TextView profile_about;

    @BindView(R.id.profile_clubs)
    TextView profile_clubs;

    @BindView(R.id.profile_friends)
    TextView profile_friends;

    @BindView(R.id.profile_created)
    TextView profile_created;

    @BindView(R.id.profile_img)
    ImageView profile_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Injector.inject(this);
        ButterKnife.bind(this);

        //Username & toolbar icon
        profile_username.setText(sharedPreferenceManager.getCurrentUsername());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        //Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.profile_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        fillViews();
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_item_profile) {
            toastCreator.showToastLong("nav_item_profile");
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
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

    private void fillViews(){

        Picasso.with(this)
                .load(Constants.basepath + "/uploads/profile/" + currentUser.getUsername())
                .error(R.drawable.google_thumb)
                .placeholder(R.drawable.google_thumb)
                .fit()
                .into(profile_img);

        if(currentUser.getFirstName()!=null){
            profile_firstname.setText(currentUser.getFirstName());
        }
        if(currentUser.getLastName()!=null){
            profile_lastname.setText(currentUser.getLastName());
        }
        if(currentUser.getAbout()!=null){
            profile_about.setText(currentUser.getAbout());
        }
        if(currentUser.getCreated()!=null){
            profile_created.setText(currentUser.getCreated());
        }
        if(currentUser.getFriends().size()>0){
            String friends = "";
            for(int i=0; i<currentUser.getFriends().size(); i++){
                if(i==currentUser.getFriends().size()-1){
                    friends += currentUser.getFriends().get(i) + "";
                }else{
                    friends += currentUser.getFriends().get(i) + ", ";
                }
            }
            profile_friends.setText("Friends: " + friends);
        }
        if(currentUser.getClubs().size()>0){
            String clubs = "";
            for(int i=0; i<currentUser.getClubs().size(); i++){
                if(i==currentUser.getClubs().size()-1){
                    clubs += currentUser.getClubs().get(i) + "";
                }else{
                    clubs += currentUser.getClubs().get(i) + ", ";
                }
            }
            profile_clubs.setText("Clubs: " + clubs);
        }
        profile_rating.setText(String.valueOf(currentUser.getRating()));
        profile_rating_big.setText(String.valueOf(currentUser.getRating()));
        profile_maxrating.setText(String.valueOf(currentUser.getMaxRating()));
    }
}
