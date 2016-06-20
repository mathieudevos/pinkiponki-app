package com.mattikettu.pinkiponki;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.ToastCreator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddGameActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_addgame)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    protected ToastCreator toastCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Injector.inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setTitle("ADD GAME");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastCreator.showToastLong("Clicked button, idiot.");
            }
        });
    }


}
