package com.mattikettu.pinkiponki.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.view.View;

import com.mattikettu.pinkiponki.ui.tabs.TabAchievements;
import com.mattikettu.pinkiponki.ui.tabs.TabClubs;
import com.mattikettu.pinkiponki.ui.tabs.TabFriends;
import com.mattikettu.pinkiponki.ui.tabs.TabHome;
import com.mattikettu.pinkiponki.util.Constants;

/**
 * Created by MD on 12/06/2016.
 */

public class ImagePagerAdapter extends FragmentPagerAdapter {

    public ImagePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount() {
        return Constants.slidingTabResID.length;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return null;
    }

    public int getDrawableID(int position){
        return Constants.slidingTabResID[position];
    }

    @Override
    public Fragment getItem(int position){
        switch (position) {
            case 0:
                return TabHome.newInstance();
            case 1:
                return new TabFriends();
            case 2:
                return new TabClubs();
            case 3:
                return new TabAchievements();
            // This should be:
            // return TabAchievements.newInstance();
        }
        return null;
    }
}
