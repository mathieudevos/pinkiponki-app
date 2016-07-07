package com.mattikettu.pinkiponki.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mathieu on 07/07/2016.
 */
public class DateTimeUtil {

    private static final String TAG = "DATETIMEUTIL";

    public DateTimeUtil(){
        Log.d(TAG, TAG + " created, use as singleton.");
        Injector.inject(this);
    }

    public String getCurrentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmmss");
        return sdf.format(new Date());
    }
}
