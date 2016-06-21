package com.mattikettu.pinkiponki;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mattikettu.pinkiponki.ui.SelectPlayerDialog;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.ToastCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddGameActivity extends AppCompatActivity {

    @BindView(R.id.rootview_addgame)
    View rootview_addgame;

    @BindView(R.id.toolbar_addgame)
    Toolbar toolbar;

    @BindView(R.id.teamA_player1)
    TextView teamA_player1;

    @BindView(R.id.teamA_player2)
    TextView teamA_player2;

    @BindView(R.id.teamB_player1)
    TextView teamB_player1;

    @BindView(R.id.teamB_player2)
    TextView teamB_player2;

    @BindView(R.id.teamA_score)
    TextView teamA_score;

    @BindView(R.id.teamB_score)
    TextView teamB_score;

    @BindView(R.id.fab_addUser)
    FloatingActionButton fab_addUser;

    @BindView(R.id.fab_upload)
    FloatingActionButton fab_upload;




    @Inject
    protected ToastCreator toastCreator;

    private long lastClick = 0;

    private int a_score = 0;
    private int b_score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Injector.inject(this);
        ButterKnife.bind(this);

        toolbar.setTitle("ADD A NEW GAME");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toastCreator.snackbarShort(rootview_addgame, "Click or double click the scores to interact");

        fab_addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPlayerDialog selectPlayerDialog = new SelectPlayerDialog();
                selectPlayerDialog.show(getFragmentManager(), "selectPlayerDialogFragment");
                selectPlayerDialog.setDialogResult(new SelectPlayerDialog.DialogResult() {
                    @Override
                    public void finish(String username, int team) {
                        addUser(username, team);
                    }
                });
            }
        });

        fab_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastCreator.showToastLong("Clicked button, idiot.");
            }
        });

        teamA_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentClick = System.currentTimeMillis();
                if((currentClick-lastClick)>500){
                    //Single click
                    a_score += 1;
                    if(a_score>6){ a_score = 0; }
                    if(a_score==6){ toastCreator.snackbarLong(rootview_addgame, "Team A wins!"); }
                }else{
                    //Double click
                    a_score = 6;
                    toastCreator.snackbarLong(rootview_addgame, "Team A wins!");
                }
                teamA_score.setText(String.valueOf(a_score));
                updateScoreColors();
                lastClick = currentClick;
            }
        });

        teamB_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentClick = System.currentTimeMillis();
                if((currentClick-lastClick)>500){
                    //Single click
                    b_score += 1;
                    if(b_score>6){ b_score = 0; }
                    if(b_score==6){ toastCreator.snackbarLong(rootview_addgame, "Team B wins!"); }
                }else{
                    //Double click
                    b_score = 6;
                    toastCreator.snackbarLong(rootview_addgame, "Team B wins!");
                }
                teamB_score.setText(String.valueOf(b_score));
                updateScoreColors();
                lastClick = currentClick;
            }
        });
    }

    private void updateScoreColors(){
        if(a_score>b_score){
            teamA_score.setTextColor(getResources().getColor(R.color.winnerGreen));
            teamB_score.setTextColor(getResources().getColor(R.color.loserRed));

        } else if(b_score>a_score){
            teamA_score.setTextColor(getResources().getColor(R.color.loserRed));
            teamB_score.setTextColor(getResources().getColor(R.color.winnerGreen));
        } else if(a_score==b_score){
            teamA_score.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            teamB_score.setTextColor(getResources().getColor(R.color.colorPrimaryText));
        }
    }

    private List<String> getCurrentPlayers(){
        List<String> currentPlayers = new ArrayList<>();
        if(!teamA_player1.getText().equals("Team A player 1")){
            currentPlayers.add(teamA_player1.getText().toString());
        }
        if(!teamA_player2.getText().equals("Team A player 2")){
            currentPlayers.add(teamA_player2.getText().toString());
        }
        if(!teamB_player1.getText().equals("Team B player 1")){
            currentPlayers.add(teamB_player1.getText().toString());
        }
        if(!teamB_player2.getText().equals("Team B player 2")){
            currentPlayers.add(teamB_player2.getText().toString());
        }
        return currentPlayers;
    }

    private void addUser(String username, int team){
        toastCreator.snackbarShort(rootview_addgame, "Adding user: " + username + " to team: " + String.valueOf(team));
    }
}
