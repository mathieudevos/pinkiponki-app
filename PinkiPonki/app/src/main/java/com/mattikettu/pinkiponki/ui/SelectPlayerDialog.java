package com.mattikettu.pinkiponki.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.mattikettu.pinkiponki.R;
import com.mattikettu.pinkiponki.networkapi.CurrentUser;
import com.mattikettu.pinkiponki.util.Injector;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by mathieu on 21/06/2016.
 */
public class SelectPlayerDialog extends DialogFragment {

    @Inject
    protected CurrentUser currentUser;

    private View dialogView;
    private long lastClick = 0;

    DialogResult dialogResult; //the callback


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Injector.inject(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_add_user, null);
        builder.setView(dialogView);
        setSpinnerFriends();
        setSpinnerRecent();
        setAddSelf();
        builder.setNegativeButton("Team A", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialogResult!=null){
                    dialogResult.finish(getUsername().toLowerCase(), 0);
                }
                SelectPlayerDialog.this.getDialog().dismiss();
            }
        });
        builder.setPositiveButton("Team B", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialogResult!=null){
                    dialogResult.finish(getUsername().toLowerCase(), 1);
                }
                SelectPlayerDialog.this.getDialog().dismiss();

            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SelectPlayerDialog.this.getDialog().cancel();

            }
        });

        return builder.create();
    }

    public interface DialogResult{
        void finish(String username, int team);
    }

    public void setDialogResult(DialogResult dialogResult){
        this.dialogResult = dialogResult;
    }

    private String getUsername(){
        EditText username = (EditText) dialogView.findViewById(R.id.input_username_dialog);
        return username.getText().toString();
    }

    private void setSpinnerFriends(){
        Spinner friendsSpinner = (Spinner) dialogView.findViewById(R.id.spinner_friends);
        List<String> friends = new ArrayList<>();
        friends.add(0, "Friends");
        if(currentUser.getFriends()!=null && currentUser.getFriends().size()>0){
            friends.addAll(currentUser.getFriends());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, friends);
        friendsSpinner.setAdapter(adapter);
        friendsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if(pos>0){
                    EditText username = (EditText) dialogView.findViewById(R.id.input_username_dialog);
                    username.setText(currentUser.getFriends().get(pos));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

    }

    private void setSpinnerRecent(){
        Spinner recentSpinner = (Spinner) dialogView.findViewById(R.id.spinner_recent);
        List<String> recentPlayedWith = new ArrayList<>();
        recentPlayedWith.add(0, "Recently played with");
        if(currentUser.getRecentPlayedWith()!= null && currentUser.getRecentPlayedWith().size()>0){
            recentPlayedWith.addAll(currentUser.getRecentPlayedWith());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, recentPlayedWith);
        recentSpinner.setAdapter(adapter);
        recentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if(pos>0){
                    EditText username = (EditText) dialogView.findViewById(R.id.input_username_dialog);
                    username.setText(currentUser.getRecentPlayedWith().get(pos));
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void setAddSelf(){
        ImageView addSelf = (ImageView) dialogView.findViewById(R.id.add_self_dialog);
        addSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText) dialogView.findViewById(R.id.input_username_dialog);
                username.setText(currentUser.getUsername());
            }
        });
    }
}
