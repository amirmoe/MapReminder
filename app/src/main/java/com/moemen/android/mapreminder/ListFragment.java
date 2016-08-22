package com.moemen.android.mapreminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by amir on 2016-08-21.
 */
public class ListFragment extends Fragment {

    private static final String TAG = "FELSÖKNING";

    private TabSelector mTabSelector;

    /**
     * This method is called upon in the pagerAdapter to switch tab view after all 10 rounds
     *
     * @param tabSelector Initialize the tabSelector
     */
    public void setTabSelector(TabSelector tabSelector) {
        mTabSelector = tabSelector;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, parent, false);

        return v;
    }
}
