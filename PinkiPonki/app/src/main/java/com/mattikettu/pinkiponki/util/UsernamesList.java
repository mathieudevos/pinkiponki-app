package com.mattikettu.pinkiponki.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MD on 20/06/2016.
 */

public class UsernamesList {

    private static final String TAG = "USERNAMESLIST";

    private List<String> usernames = new ArrayList<>();

    public UsernamesList(){
        Log.d(TAG, TAG + " created, use as singleton.");
        Injector.inject(this);
    }

    public void fullUpdate(List<String> usernames){
        Log.d(TAG, "Total users count: " + usernames.size());
        this.usernames.addAll(usernames);
    }

    public boolean contains(String username){
        return usernames.contains(username.toLowerCase());
    }
}
