package com.moemen.android.mapreminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by amir on 2016-08-21.
 */
public class ListFragment extends Fragment {

    private static final String TAG = "FELSÖKNING";
    private TabSelector mTabSelector;


    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);



        return v;
    }

    public void sendArray(ArrayList list){
        mAdapter.set(list);
        mAdapter.notifyDataSetChanged();
    }
}
