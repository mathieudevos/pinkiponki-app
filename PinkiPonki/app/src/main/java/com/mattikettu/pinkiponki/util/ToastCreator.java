package com.mattikettu.pinkiponki.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
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

    public void showToastShort(Context ctx, String text){
        Toast.makeText(ctx, text,Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(Context ctx, String text){
        Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
    }

    public void snackbarLong(View v, String text){
        Snackbar.make(v, text, Snackbar.LENGTH_LONG).show();
    }

    public void snackbarShort(View v, String text){
        Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show();
    }
}
