package com.mattikettu.pinkiponki.util;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

import com.mattikettu.pinkiponki.networkapi.CAservice;

/**
 * Created by MD on 10/06/2016.
 **/

public class BootstrapApplication extends Application{

    private static BootstrapApplication instance;

    /**
     * Create main application
     */
    public BootstrapApplication() {
    }

    /**
     * Create main application
     *
     * @param context
     */
    public BootstrapApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;



        // Perform injection
        Injector.init(getRootModule(), this);

        startService(new Intent(getApplicationContext(), CAservice.class));
    }

    private Object getRootModule() {
        return new RootModule();
    }


    /**
     * Create main application
     *
     * @param instrumentation
     */
    public BootstrapApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static BootstrapApplication getInstance() {
        return instance;
    }

}
