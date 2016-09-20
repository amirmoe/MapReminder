package com.moemen.android.mapreminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Second view which main purpose is to show a list of all the markers.
 */
public class ListFragment extends Fragment {

    private static final String TAG = "FELSÖKNING";
    private Communicator comm;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView emptyText;

    /**
     * This method is called upon in the pagerAdapter to make it able for the fragments to communicate
     * with each other
     *
     * @param communicator Initialize the communicator
     */
    public void setCommunicator(Communicator communicator) {
        this.comm = communicator;
    }

    /**
     * This method is called upon in the pagerAdapter when the elements of the array list with
     * markers has been changed.
     * @param list arraylist with all the markers.
     */
    public void sendArray(ArrayList list){
        if (list.size()!=0){
            emptyText.setVisibility(View.INVISIBLE);
        }
        mAdapter.set(list);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * Initiate the recyclerView
     *
     * @param inflater inflates xml to give a view
     * @param parent Fragments parent ViewGroup
     * @param savedInstanceState Prior state bundle
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, parent, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        emptyText = (TextView) v.findViewById(R.id.emptyText);


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
            /**
             * called upon from myAdapter when the Remove button has been pressed. updates the list
             * and recyclerView.
             *
             * @param pos position of item to remove
             * @param list the Arraylist of markers.
             */
            @Override
            public void onClick(int pos, ArrayList list) {
                comm.positionToRemove(pos);
                mRecyclerView.removeViewAt(pos);
                mAdapter.notifyItemRemoved(pos);
                mAdapter.notifyItemRangeChanged(pos, list.size());
                mAdapter.notifyDataSetChanged();

                if (list.size()==0){
                    emptyText.setVisibility(View.VISIBLE);
                }
            }
        });


        return v;
    }



}
