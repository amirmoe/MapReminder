package com.moemen.android.mapreminder;

import java.util.ArrayList;

/**
 * Interface to allow MyAdapter to communicate with ListFragment.
 */
interface ListFragmentCommunication {
    /**
     * called upon from myAdapter when the Remove button has been pressed. updates the list
     * and recyclerView.
     *
     * @param position position of item to remove
     * @param list the arrayList of markers.
     */
    void removeMarkerPosition(int position, ArrayList list);
    /**
     * called upon from myAdapter when the change message button has been pressed.
     * Tells the MapFragment which marker message needs to be changed.
     * @param position position of item to change message.
     * @param message the new marker message.
     */
    void addMessageMarkerPosition(int position, String message);

}
