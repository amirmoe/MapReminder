package com.moemen.android.mapreminder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by amir on 2016-08-21.
 */
public class ListFragment extends Fragment {

    private static final String TAG = "FELSÖKNING";

    private Communicator comm;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    /**
     * This method is called upon in the pagerAdapter to make it able for the fragments to communicate
     * with each other
     *
     * @param communicator Initialize the communicator
     */
    public void setCommunicator(Communicator communicator) {
        this.comm = communicator;
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

        mAdapter.setRemoveMarkerPosition(new RemoveMarkerPosition() {
            @Override
            public void onClick(int pos, ArrayList list) {
                comm.positionToRemove(pos);
                Log.d(TAG, "ListFragment: -" + Integer.toString(list.size()));


                Log.d(TAG, "Pos jag ska tabort" + Integer.toString(pos));
                //list.remove(pos);
                mRecyclerView.removeViewAt(pos);
                mAdapter.notifyItemRemoved(pos);
                mAdapter.notifyItemRangeChanged(pos, list.size());
                mAdapter.notifyDataSetChanged();
            }
        });


        return v;
    }

    public void sendArray(ArrayList list){
        mAdapter.set(list);
        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "ListFragment: +" + Integer.toString(list.size()));



    }

}
