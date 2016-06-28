package com.mattikettu.pinkiponki.networkapi;

import android.util.Log;

import com.mattikettu.pinkiponki.objects.UserObject;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by mathieu on 10/06/2016.
 */
public class CurrentUser extends UserObject {

    @Inject
    protected SharedPreferenceManager sharedPreferenceManager;

    private static final String TAG = "CURRENTUSER";
    private List<String> clubs = new ArrayList<>();
    private List<String> friends = new ArrayList<>();
    private List<String> friendsTimeline = new ArrayList<>();
    private List<String> games = new ArrayList<>();

    public CurrentUser(){
        Log.d(TAG, TAG + " created, use as singleton");
        Injector.inject(this);
    }

    public void fullUpdate(UserObject user){
        this.setUsername(user.getUsername());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setAbout(user.getAbout());
        this.setEmail(user.getEmail());
        clubs.addAll(user.getClubs());
        this.setCreated(user.getCreated());
        friends.addAll(user.getFriends());
        friendsTimeline.addAll(user.getFriendsTimeline());
        this.setLastSeen(user.getLastSeen());
        games.addAll(user.getGames());
        this.setMaxRating(user.getMaxRating());
        this.setRating(user.getRating());
        this.setPassword("Suck da dickah");

        sharedPreferenceManager.setCurrentEmail(user.getEmail());
    }

    public List<String> getRecentPlayedWith(){
        List<String> recentPlayedWith = new ArrayList<>();
        if(getGames()!=null && getGames().size()>0){
            for(int i = 0; i<Math.min(getGames().size(),3);i++){
                //Only check last 3
            }
        }
        recentPlayedWith.add("me");
        recentPlayedWith.add("myself");
        recentPlayedWith.add("and I");
        return recentPlayedWith;
    }
}
