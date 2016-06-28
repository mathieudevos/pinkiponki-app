package com.mattikettu.pinkiponki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.SharedPreferenceManager;

import javax.inject.Inject;

public class WelcomeActivity extends AppCompatActivity {

    @Inject
    protected SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Injector.inject(this);
    }

    public void sendWelcomeButton(View v){
        sharedPreferenceManager.setInitialLaunchDone();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

    // https://github.com/AndroidSources/Android-Splash-Screen-and-Welcome-Screen-Tutorial/
}
