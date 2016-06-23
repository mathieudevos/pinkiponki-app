package com.mattikettu.pinkiponki;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mattikettu.pinkiponki.networkapi.CurrentUser;
import com.mattikettu.pinkiponki.networkapi.NetworkLogic;
import com.mattikettu.pinkiponki.objects.GameObject;
import com.mattikettu.pinkiponki.ui.SelectPlayerDialog;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.LocationsList;
import com.mattikettu.pinkiponki.util.ToastCreator;
import com.mattikettu.pinkiponki.util.UsernamesList;

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

    @BindView(R.id.location)
    Spinner location;

    @BindView(R.id.description_addgame)
    EditText description_addgame;

    @Inject
    protected NetworkLogic NWL;

    @Inject
    protected ToastCreator toastCreator;

    @Inject
    protected UsernamesList usernamesList;

    @Inject
    protected LocationsList locationsList;

    @Inject
    protected CurrentUser currentUser;

    private long lastClick = 0;

    private int a_score = 0;
    private int b_score = 0;

    private Handler handler;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        Injector.inject(this);
        ButterKnife.bind(this);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 200:
                        uploadGameSuccess((GameObject) msg.obj);
                        break;
                    default:
                        uploadGameFailure();
                        break;
                }
            }
        };

        toolbar.setTitle("ADD A NEW GAME");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toastCreator.showToastLong("Click or double click the scores to interact");

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
                uploadGame();
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

        setDoubleTapToRemove();
        List<String> locations = locationsList.getLocations();
        locations.add(0, "Location");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_secondary_text, locations);
        location.setAdapter(adapter);
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
            toastCreator.snackbarShort(rootview_addgame, "No draws! Change that shit.");
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
        if(usernamesList.contains(username)){
            if(alreadyPlaying(username)){
                toastCreator.snackbarLong(rootview_addgame, "Player is already in the game, double tap to remove.");
                return;
            }
            if(team==0){
                if(teamA_player1.getText().equals("Team A player 1")){
                    teamA_player1.setText(username);
                    teamA_player1.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                    return;
                }
                if(teamA_player2.getText().equals("Team A player 2")){
                    teamA_player2.setText(username);
                    teamA_player2.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                    return;
                }
                toastCreator.snackbarLong(rootview_addgame, "Users for team A already full, doubletap on a user to remove.");
                return;
            }
            if(team==1){
                if(teamB_player1.getText().equals("Team B player 1")){
                    teamB_player1.setText(username);
                    teamB_player1.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                    return;
                }
                if(teamB_player2.getText().equals("Team B player 2")){
                    teamB_player2.setText(username);
                    teamB_player2.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                    return;
                }
                toastCreator.snackbarLong(rootview_addgame, "Users for team A already full, doubletap on a user to remove.");
                return;
            }
        }else {
            toastCreator.snackbarShort(rootview_addgame, "Cannot find user: " + username);
        }
    }

    private boolean alreadyPlaying(String username){
        boolean playing = false;
        if(teamA_player1.getText().equals(username)){
            playing = true;
        }
        if(teamA_player2.getText().equals(username)){
            playing = true;
        }
        if(teamB_player1.getText().equals(username)){
            playing = true;
        }
        if(teamB_player2.getText().equals(username)){
            playing = true;
        }
        return playing;
    }

    private void setDoubleTapToRemove(){
        teamA_player1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentClick = System.currentTimeMillis();
                if((currentClick-lastClick)<500){
                    teamA_player1.setText("Team A player 1");
                    teamA_player1.setTextColor(getResources().getColor(R.color.colorSecondaryText));
                }
                lastClick = currentClick;
            }
        });
        teamA_player2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentClick = System.currentTimeMillis();
                if((currentClick-lastClick)<500){
                    teamA_player2.setText("Team A player 2");
                    teamA_player2.setTextColor(getResources().getColor(R.color.colorSecondaryText));
                }
                lastClick = currentClick;
            }
        });
        teamB_player1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentClick = System.currentTimeMillis();
                if((currentClick-lastClick)<500){
                    teamB_player1.setText("Team B player 1");
                    teamB_player1.setTextColor(getResources().getColor(R.color.colorSecondaryText));
                }
                lastClick = currentClick;
            }
        });
        teamB_player2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentClick = System.currentTimeMillis();
                if((currentClick-lastClick)<500){
                    teamB_player2.setText("Team B player 2");
                    teamB_player2.setTextColor(getResources().getColor(R.color.colorSecondaryText));
                }
                lastClick = currentClick;
            }
        });
    }

    private void uploadGame(){
        if(progressDialog==null){
            progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        }
        if(!progressDialog.isShowing()){
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Uploading the game");
            progressDialog.show();
        }

        List<String> players = getCurrentPlayers();
        if(players.size()!=4){
            toastCreator.snackbarLong(rootview_addgame, "Need 4 players!");
            return;
        }
        if(Integer.parseInt(teamA_score.getText().toString())!=6 && Integer.parseInt(teamB_score.getText().toString())!=6){
            toastCreator.snackbarLong(rootview_addgame, "Needs a winner!");
            return;
        }
        if(Integer.parseInt(teamA_score.getText().toString()) == Integer.parseInt(teamB_score.getText().toString())){
            toastCreator.snackbarLong(rootview_addgame, "Draws are for losers!");
            return;
        }
        boolean currentUserIsPlayer = false;
        for(int i=0; i<players.size();i++){
            if(players.get(i).toLowerCase().equals(currentUser.getUsername().toLowerCase())){
                currentUserIsPlayer = true;
            }
        }
        if(!currentUserIsPlayer){
            toastCreator.snackbarLong(rootview_addgame, "You have to be played, unable to upload for others!");
            return;
        }
        String locationString = "";
        if(!location.getSelectedItem().toString().equals("Location")){
            locationString = location.getSelectedItem().toString();
        }

        GameObject game = new GameObject();

        //players
        game.setTeamA_player1(players.get(0).toLowerCase());
        game.setTeamA_player2(players.get(1).toLowerCase());
        game.setTeamB_player1(players.get(2).toLowerCase());
        game.setTeamB_player1(players.get(3).toLowerCase());

        //author
        game.setAuthor(currentUser.getUsername().toLowerCase());

        //score
        game.setTeamA_score(Integer.parseInt(teamA_score.getText().toString()));
        game.setTeamB_score(Integer.parseInt(teamB_score.getText().toString()));

        //location
        game.setLocation(locationString);

        //about
        game.setAbout(description_addgame.getText().toString());
    }

    private void uploadGameSuccess(GameObject game){

    }

    private void uploadGameFailure(){

    }
}
