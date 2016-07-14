package com.mattikettu.pinkiponki;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattikettu.pinkiponki.networkapi.CurrentUser;
import com.mattikettu.pinkiponki.networkapi.NetworkLogic;
import com.mattikettu.pinkiponki.objects.GameObject;
import com.mattikettu.pinkiponki.objects.UserObject;
import com.mattikettu.pinkiponki.ui.ImageCropperActivity;
import com.mattikettu.pinkiponki.util.Constants;
import com.mattikettu.pinkiponki.util.DateTimeUtil;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.SharedPreferenceManager;
import com.mattikettu.pinkiponki.util.ToastCreator;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "EDITPROFILEACTIVYT";


    @Inject
    protected ToastCreator toastCreator;

    @Inject
    protected SharedPreferenceManager sharedPreferenceManager;

    @Inject
    protected CurrentUser currentUser;

    @Inject
    protected NetworkLogic NWL;

    @Inject
    protected DateTimeUtil DTU;

    private boolean updateProfilePicture = false;

    private String picturename = null;

    @BindView(R.id.edit_profile_username)
    TextView edit_profile_username;

    @BindView(R.id.edit_profile_rating_big)
    TextView edit_profile_rating_big;

    @BindView(R.id.edit_profile_rating)
    TextView edit_profile_rating;

    @BindView(R.id.edit_profile_maxrating)
    TextView edit_profile_maxrating;

    @BindView(R.id.edit_profile_firstname)
    EditText edit_profile_firstname;

    @BindView(R.id.edit_profile_lastname)
    EditText edit_profile_lastname;

    @BindView(R.id.edit_profile_about)
    EditText edit_profile_about;

    @BindView(R.id.edit_profile_clubs)
    TextView edit_profile_clubs;

    @BindView(R.id.edit_profile_friends)
    TextView edit_profile_friends;

    @BindView(R.id.edit_profile_created)
    TextView edit_profile_created;

    @BindView(R.id.edit_profile_img)
    ImageView edit_profile_img;

    @BindView(R.id.edit_profile_img_update)
    ImageView edit_profile_img_update;

    private ProgressDialog progressDialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ButterKnife.bind(this);
        Injector.inject(this);

        //Hanlder
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 200:
                        if(msg.arg1==0){
                            updateSuccess();
                        }
                        if(msg.arg1==1){
                            updateImageSuccess();
                        }
                        break;
                    default:
                        failOccurred();
                        break;
                }
            }
        };

        //Username & toolbar icon
        edit_profile_username.setText(sharedPreferenceManager.getCurrentUsername());
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_profile_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        fillViews();
        edit_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageCropperActivity.class);
                intent.putExtra("originActivity", 901);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        Log.d(TAG, "onResume called.");

        //final step before a running activity, check extra from inten to see if we got a bitmap from cropped shizznits.
        if(getIntent().hasExtra("image")){
            Log.d(TAG, "We have an image in extra intent!");
            byte[] bytes = getIntent().getByteArrayExtra("image");
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
            edit_profile_img.setImageBitmap(bm);
            picturename = getIntent().getStringExtra("imagename");
            toastCreator.showToastShort("Image name: " + picturename);
            updateProfilePicture = true;
        }else{
            Log.d(TAG, "No intent, just putting the base value");
            Picasso.with(this)
                    .load(Constants.basepath + "/uploads/profile/" + currentUser.getUsername())
                    .error(R.drawable.google_thumb)
                    .placeholder(R.drawable.google_thumb)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .into(edit_profile_img);
        }
        super.onResume();
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
        if(currentUser.getFirstName()!=null){
            edit_profile_firstname.setText(currentUser.getFirstName());
        }
        if(currentUser.getLastName()!=null){
            edit_profile_lastname.setText(currentUser.getLastName());
        }
        if(currentUser.getAbout()!=null){
            edit_profile_about.setText(currentUser.getAbout());
        }
        if(currentUser.getCreated()!=null){
            edit_profile_created.setText(currentUser.getCreated());
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
            edit_profile_friends.setText("Friends: " + friends);
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
            edit_profile_clubs.setText("Clubs: " + clubs);
        }
        edit_profile_rating.setText(String.valueOf(currentUser.getRating()));
        edit_profile_rating_big.setText(String.valueOf(currentUser.getRating()));
        edit_profile_maxrating.setText(String.valueOf(currentUser.getMaxRating()));
    }

    private void updateProfile(){
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating profile...");
        progressDialog.show();
        NWL.updateProfile(getUserFromFields(), handler);
        if(updateProfilePicture){
            NWL.updateProfilePicture(picturename, ((BitmapDrawable) edit_profile_img.getDrawable()).getBitmap(), handler);
        }
    }

    private UserObject getUserFromFields(){
        UserObject userObject = currentUser;

        userObject.setFirstName(edit_profile_firstname.getText().toString());
        userObject.setLastName(edit_profile_lastname.getText().toString());
        userObject.setAbout(edit_profile_about.getText().toString());

        return userObject;
    }

    private void updateSuccess(){
        if(!updateProfilePicture){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            toastCreator.showToastLong("Updating user successful.");
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private void updateImageSuccess(){
        updateProfilePicture = false;
        updateSuccess();
    }

    private void failOccurred(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        toastCreator.snackbarLong(this.getCurrentFocus(), "Updating user/image failed.");
    }
}
