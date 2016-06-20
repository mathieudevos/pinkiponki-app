package com.mattikettu.pinkiponki.ui.tabs;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.mattikettu.pinkiponki.AddGameActivity;
import com.mattikettu.pinkiponki.R;
import com.mattikettu.pinkiponki.networkapi.CurrentUser;
import com.mattikettu.pinkiponki.networkapi.NetworkLogic;
import com.mattikettu.pinkiponki.objects.GameObject;
import com.mattikettu.pinkiponki.objects.UserObject;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.SharedPreferenceManager;
import com.mattikettu.pinkiponki.util.ToastCreator;
import com.mattikettu.pinkiponki.util.UsernamesList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by MD on 12/06/2016.
 */



public class TabHome extends Fragment implements AdapterView.OnItemClickListener{

    private static final String TAG = "TABHOME";

    @BindView(R.id.swipeContainer_home)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    protected ToastCreator toastCreator;

    @Inject
    protected NetworkLogic NWL;

    @Inject
    protected CurrentUser currentUser;

    @Inject
    protected UsernamesList usernamesList;

    @Inject
    protected SharedPreferenceManager sharedPreferenceManager;

    private TabHomeAdapter tabHomeAdapter = null;
    private List<GameObject> games = new ArrayList<>();

    private ListView lv;
    private Handler handler;
    private ProgressDialog progressDialog;
    private View footerView;
    private View thisView;

    private String pdialogMsg = "Acquiring information...";
    private int currentDone = 0;

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

        Injector.inject(this);
        ButterKnife.bind(this, v);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 200:
                        //Got the good response, now check arg1 for what it is.
                        switch (msg.arg1){
                            case 1:
                                //We got our user
                                updateForCurrentUser((UserObject) msg.obj);
                                break;
                            case 2:
                                //We got all the games
                                updateForGames((List<GameObject>) msg.obj);
                                break;
                            case 3:
                                //We got the usernames list
                                updateForUsernames((List<String>) msg.obj);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        failOccurred();
                }
            }
        };

        //Fab
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddGameActivity.class);
                startActivity(intent);

                toastCreator.showToastLong("Clicked button, idiot.");
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadEverythingNoDialog();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.tabsScrollColor);

        loadEverythingDialog();

        thisView = v;

        return v;
    }

    @Override
    public void onStart(){
        super.onStart();

        lv = (ListView) getActivity().findViewById(R.id.tab_home_listview);
        lv.setOnItemClickListener(this);

        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        if(lv.getFooterViewsCount()==0){
            lv.addFooterView(footerView);
        }
        lv.setEmptyView(getActivity().findViewById(R.id.empty_view));

        lv.setAdapter(getTabHomeAdapter());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toastCreator.snackbarLong(view, "You clicked item: " + position);
    }

    private void loadEverythingNoDialog(){
        loadUser();
        loadAllGames();
        loadAllUsernames();
    }

    private void loadEverythingDialog(){
        progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(pdialogMsg);
        progressDialog.show();
        loadUser();
        loadAllGames();
        loadAllUsernames();
    }

    private void failOccurred(){
        if(progressDialog.isShowing()){
            pdialogMsg = "Acquiring information...";
            progressDialog.dismiss();
        }
        toastCreator.snackbarLong(thisView, "Acquiring info failed.");
    }

    private void updateForGames(List<GameObject> games){
        this.games = games;
        pdialogMsg += "\nGames loaded.";
        if(progressDialog.isShowing()){
            progressDialog.setMessage(pdialogMsg);
        }
        checkedUpdates();
    }

    private void updateForCurrentUser(UserObject userObject){
        currentUser.fullUpdate(userObject);
        pdialogMsg += "\n" + userObject.getUsername() + " loaded.";
        if(progressDialog.isShowing()){
            progressDialog.setMessage(pdialogMsg);
        }
        checkedUpdates();
    }

    private void updateForUsernames(List<String> usernames){
        usernamesList.fullUpdate(usernames);
        pdialogMsg += "\nUsernames loaded.";
        if(progressDialog.isShowing()){
            progressDialog.setMessage(pdialogMsg);
        }
        checkedUpdates();
    }

    private void loadUser(){
        NWL.getUser(sharedPreferenceManager.getCurrentUsername(), handler);
    }

    private void loadAllGames(){
        NWL.getGames(sharedPreferenceManager.getDefaultGameAmount(), handler);
    }

    private void loadAllUsernames(){
        NWL.getUsernames(handler);
    }

    private void checkedUpdates(){
        currentDone += 1;
        if(currentDone == 3){
            allUpdatesFinished();
            currentDone=0;
        }
    }

    private void allUpdatesFinished(){
        if(progressDialog.isShowing()){
            pdialogMsg = "Acquiring information...";
            progressDialog.dismiss();
        }
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        if(lv!=null || games.size()>0){
            ((TabHomeAdapter) ((HeaderViewListAdapter) lv.getAdapter()).getWrappedAdapter()).clear();
            ((TabHomeAdapter) ((HeaderViewListAdapter) lv.getAdapter()).getWrappedAdapter()).addAll(games);
            ((TabHomeAdapter) ((HeaderViewListAdapter) lv.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
        }
        toastCreator.snackbarLong(thisView, "Acquiring info completed.");
    }

}
