package com.mattikettu.pinkiponki.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by mathieu on 10/06/2016.
 */


public class ToastCreator {

    private static final String TAG = "TOASTCREATOR";

    @Inject protected Context context;

    public ToastCreator(){
        Log.d(TAG, TAG + " created, use as singleton.");
        Injector.inject(this);
    }

    public void showToastShort(String text){
        Toast.makeText(context, text,Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
