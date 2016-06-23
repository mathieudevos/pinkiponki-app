package com.mattikettu.pinkiponki.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mathieu on 23/06/2016.
 */

public class LocationsList {

    private static final String TAG = "LOCATIONSLIST";

    private List<String> locations = new ArrayList<>();

    public LocationsList(){
        Log.d(TAG, TAG + " created, use as singleton.");
        Injector.inject(this);
    }

    public void fullUpdate(List<String> locations){
        Log.d(TAG, "Total locations count: " + locations.size());
        this.locations.addAll(locations);
    }

    public boolean contains(String location){return locations.contains(location); }

    public List<String> getLocations(){ return locations; }
}
