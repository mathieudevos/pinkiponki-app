package com.mattikettu.pinkiponki.util;

import android.util.Log;

/**
 * Created by mathieu on 14/06/2016.
 */
public class SharedPreferenceManager {

    private static final String TAG = "SHAREDPREFERENCEMANAGER";

    public SharedPreferenceManager(){
        Injector.inject(this);
        Log.d(TAG, TAG + " created, singleton usage.");
    }
}
