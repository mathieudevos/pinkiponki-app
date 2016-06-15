package com.mattikettu.pinkiponki.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;

/**
 * Created by mathieu on 14/06/2016.
 */
public class SharedPreferenceManager {

    @Inject
    protected Context appContext;

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
        editor.putBoolean("initialLaunch", false);
        editor.commit();
    }
}
