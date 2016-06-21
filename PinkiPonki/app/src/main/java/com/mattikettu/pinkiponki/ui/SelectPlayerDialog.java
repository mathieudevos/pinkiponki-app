package com.mattikettu.pinkiponki.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import com.mattikettu.pinkiponki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mathieu on 21/06/2016.
 */
public class SelectPlayerDialog extends DialogFragment {

    DialogResult dialogResult; //the callback


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final EditText usernameEditText = new EditText(getActivity());
        usernameEditText.setHint("Username");
        usernameEditText.setPadding(30,50,30,50);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a player");
        builder.setIcon(R.drawable.ic_person_add_black_24dp);
        builder.setView(usernameEditText);
        builder.setNegativeButton("Team A", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialogResult!=null){
                    dialogResult.finish(usernameEditText.getText().toString(), 0);
                }
                SelectPlayerDialog.this.getDialog().dismiss();
            }
        });
        builder.setPositiveButton("Team B", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialogResult!=null){
                    dialogResult.finish(usernameEditText.getText().toString(), 1);
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
}
