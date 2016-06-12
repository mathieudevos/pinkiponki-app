package com.mattikettu.pinkiponki.ui.tabs;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mattikettu.pinkiponki.R;


/**
 * Created by MD on 12/06/2016.
 */

public class TabHome extends Fragment {
    
    //// TODO: 12/06/2016 change to listFragment, fill properly.

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_home,container,false);
        return v;
    }

}
