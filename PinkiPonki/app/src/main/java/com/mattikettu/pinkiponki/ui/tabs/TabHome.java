package com.mattikettu.pinkiponki.ui.tabs;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mattikettu.pinkiponki.R;
import com.mattikettu.pinkiponki.objects.GameObject;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.ToastCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by MD on 12/06/2016.
 */



public class TabHome extends Fragment implements AdapterView.OnItemClickListener{

    private static final String TAG = "TABHOME";

    @Inject
    protected ToastCreator toastCreator;

    private TabHomeAdapter tabHomeAdapter = null;
    private List<GameObject> games = new ArrayList<>();

    public static TabHome newInstance(){
        TabHome tabHome = new TabHome();
        Bundle b = new Bundle();
        tabHome.setArguments(b);
        tabHome.getId();
        Log.d(TAG, TAG + " created.");
        return tabHome;
    }

    private TabHomeAdapter getTabHomeAdapter(){
        if(tabHomeAdapter == null){
            tabHomeAdapter = new TabHomeAdapter(getActivity(), R.layout.row_tabhome, games);
        }
        return tabHomeAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_home,container,false);

        //Fab
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toastCreator.showToastLong("Clicked button, idiot.");
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });


        Injector.inject(this);
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        ListView lv = (ListView) getActivity().findViewById(R.id.tab_home_listview);
        lv.setAdapter(getTabHomeAdapter());
        lv.setOnItemClickListener(this);

        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        lv.addFooterView(footerView);
        lv.setEmptyView(getActivity().findViewById(R.id.empty_view));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toastCreator.showToastLong("You clicked item: " + position);
    }

}
