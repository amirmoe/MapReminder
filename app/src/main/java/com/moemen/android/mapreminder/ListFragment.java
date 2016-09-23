package com.moemen.android.mapreminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Second view which main purpose is to show a list of all the markers.
 */
public class ListFragment extends Fragment {

    private Communicator mCommunicator;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private TextView emptyText;

    /**
     * This method is called upon in the pagerAdapter to make it able for the fragments to communicate
     * with each other
     *
     * @param communicator Initialize the communicator
     */
    public void setCommunicator(Communicator communicator) {
        this.mCommunicator = communicator;
    }

    /**
     * This method is called upon in the pagerAdapter when the elements of the arrayList with
     * markers has been changed.
     * @param list arrayList with all the markers.
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

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setListFragmentCommunication(new ListFragmentCommunication() {
            /**
             * called upon from myAdapter when the Remove button has been pressed. updates the list
             * and recyclerView.
             *
             * @param position position of item to remove
             * @param list the arrayList of markers.
             */
            @Override
            public void removeMarkerPosition(int position, ArrayList list) {
                mCommunicator.positionToRemove(position);
                mRecyclerView.removeViewAt(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, list.size());
                mAdapter.notifyDataSetChanged();

                if (list.size()==0){
                    emptyText.setVisibility(View.VISIBLE);
                }
            }

            /**
             * called upon from myAdapter when the change message button has been pressed.
             * Tells the MapFragment which marker message needs to be changed.
             * @param position position of item to change message.
             * @param message the new marker message.
             */
            @Override
            public void addMessageMarkerPosition(int position, String message) {
                mCommunicator.positionToAddMessage(position, message);
            }
        });


        return v;
    }



}
