package com.mattikettu.pinkiponki.ui.tabs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mattikettu.pinkiponki.R;
import com.mattikettu.pinkiponki.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MD on 13/06/2016.
 */

public class TabHomeAdapter extends ArrayAdapter<GameObject>{

    private static final String TAG = "TABHOMEADAPTER";

    private List<GameObject> games = new ArrayList<>();
    private Context context;

    public TabHomeAdapter(Context ctx, int resource, List<GameObject> games){
        super(ctx, resource, games);
        context = ctx;
        this.games = games;
        Log.d(TAG, TAG + " created.");
    }

    @Override
    public View getView(final int position, View concertView, ViewGroup parent){
        View v = concertView;
        if(v == null){
            v = LayoutInflater.from(context).inflate(R.layout.row_tabhome, parent, false);
        }

        return v;
    }
}
