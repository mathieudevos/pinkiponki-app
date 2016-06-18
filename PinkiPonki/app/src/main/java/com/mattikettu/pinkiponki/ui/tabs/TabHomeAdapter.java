package com.mattikettu.pinkiponki.ui.tabs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattikettu.pinkiponki.R;
import com.mattikettu.pinkiponki.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    static class ViewHolder {

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

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.a1_verify)
        ImageView a1_verify;

        @BindView(R.id.a2_verify)
        ImageView a2_verify;

        @BindView(R.id.b1_verify)
        ImageView b1_verify;

        @BindView(R.id.b2_verify)
        ImageView b2_verify;

        public ViewHolder(View v){
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public View getView(final int position, View concertView, ViewGroup parent){
        View v = concertView;
        ViewHolder holder;
        if(v == null){
            v = LayoutInflater.from(context).inflate(R.layout.row_tabhome, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        GameObject game = games.get(position);

        // handle everything through the holder.

        // Players
        holder.teamA_player1.setText(game.getTeamA_player1());
        holder.teamA_player2.setText(game.getTeamA_player2());
        holder.teamB_player1.setText(game.getTeamB_player1());
        holder.teamB_player2.setText(game.getTeamB_player2());

        // Score
        holder.teamA_score.setText(Integer.toString(game.getTeamA_score()));
        holder.teamB_score.setText(Integer.toString(game.getTeamB_score()));

        // Verified icons
        for(int i  = 0; i<game.getVerification().size(); i++){
            if(game.getVerification().get(i).equals(game.getTeamA_player1())) {
                holder.a1_verify.setVisibility(View.VISIBLE);
            }
            if(game.getVerification().get(i).equals(game.getTeamA_player2())) {
                holder.a2_verify.setVisibility(View.VISIBLE);
            }
            if(game.getVerification().get(i).equals(game.getTeamB_player1())) {
                holder.b1_verify.setVisibility(View.VISIBLE);
            }
            if(game.getVerification().get(i).equals(game.getTeamB_player2())) {
                holder.b2_verify.setVisibility(View.VISIBLE);
            }
        }

        // Timestamp
        holder.timestamp.setText(game.getTimestamp());

        return v;
    }
}
