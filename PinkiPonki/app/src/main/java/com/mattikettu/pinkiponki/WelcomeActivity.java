package com.mattikettu.pinkiponki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void sendWelcomeButton(View v){
        Intent intent = new Intent(getApplication(), LoginActivity.class);
        startActivity(intent);
    }

    // https://github.com/AndroidSources/Android-Splash-Screen-and-Welcome-Screen-Tutorial/
}
