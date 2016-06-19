package com.mattikettu.pinkiponki.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mattikettu.pinkiponki.networkapi.CurrentUser;
import com.mattikettu.pinkiponki.objects.Username;

import javax.inject.Inject;

/**
 * Created by mathieu on 14/06/2016.
 */
public class SharedPreferenceManager {

    @Inject
    protected Context appContext;

    @Inject
    protected CurrentUser currentUser;

    private static final String TAG = "SHAREDPREFERENCEMANAGER";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public SharedPreferenceManager(){
        Injector.inject(this);
        Log.d(TAG, TAG + " created, singleton usage.");
        sharedPreferences = appContext.getSharedPreferences(Constants.sharedPreferences, Context.MODE_PRIVATE);


        editor = sharedPreferences.edit();
    }

    public boolean isInitialLaunch(){
        return sharedPreferences.getBoolean("initialLaunch", true);
    }

    public void setInitialLaunchDone(){
        Log.d(TAG, "Initial launch is done, no longer displaying welcome screen.");
        editor.putBoolean("initialLaunch", false);
        editor.commit();
    }

    public void setCurrentUsername(Username username){
        Log.d(TAG, "CurrentUsername set: " + username.getUsername());
        editor.putString("CurrentUsername", username.getUsername());
        editor.commit();
    }

    public String getCurrentUsername(){
        Log.d(TAG, "CurrentUsername get: " + sharedPreferences.getString("CurrentUsername", ""));
        return sharedPreferences.getString("CurrentUsername", "");
    }

    public int getDefaultGameAmount(){
        Log.d(TAG, "Get default game amount: " + sharedPreferences.getInt("defaultGameAmount", 20));
        return sharedPreferences.getInt("defaultGameAmount", 20);
    }

    public void setCurrentEmail(String email){
        Log.d(TAG, "CurrentEmail set: " + email);
        editor.putString("CurrentEmail", email);
        editor.commit();
    }

    public String getCurrentEmail(){
        Log.d(TAG, "CurrentEmail get: " + sharedPreferences.getString("CurrentEmail", "error@hupsista.komski"));
        return sharedPreferences.getString("CurrentEmail", "error@hupsista.komski");
    }

    public void softReset(){
        Log.d(TAG, "Soft reset called.");
        editor.clear();
        editor.commit();
        setInitialLaunchDone();
    }

    public void hardReset(){
        Log.d(TAG, "Hard reset called.");
        editor.clear();
        editor.commit();
    }
}
