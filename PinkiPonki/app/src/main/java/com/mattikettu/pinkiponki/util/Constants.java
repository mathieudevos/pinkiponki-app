package com.mattikettu.pinkiponki.util;

import android.os.Environment;

import com.mattikettu.pinkiponki.R;

import java.io.File;

/**
 * Created by mathieu on 10/06/2016.
 */
public final class Constants {

    // simple class for constants
    public static final String sharedPreferences = "PINKIPONKI_SHARED_PREF";
    public static final int[] slidingTabResID = {
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_group_black_24dp,
            R.drawable.ic_group_work_black_24dp,
            R.drawable.trophy
    };
    public static final String basepath = "http://ec2-52-58-165-57.eu-central-1.compute.amazonaws.com:9721";
    public static final File picturepath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Pinkiponki");
    public static final String username_pattern = "^[a-zA-Z0-9]{4,20}$";
    public static final String password_pattern = "^[a-zA-Z0-9@-_^!?+-/*.,:;<>]{8,32}$";
}
