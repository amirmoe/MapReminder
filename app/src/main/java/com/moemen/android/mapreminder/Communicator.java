package com.moemen.android.mapreminder;

import java.util.ArrayList;

/**
 * Interface to allow ListFragment and MapFragment to communicate with each other.
 */
interface Communicator {
    /**
     * Callback used for fragment MapFragment to communicate with
     * fragment ListFragment. Updates the arrayList with markers.
     * @param list update of the markers
     */
    void arrayToList(ArrayList list);
    /**
     * Callback used for fragment ListFragment to communicate with
     * fragment MapFragment. Tells MapFragment which marker to remove
     * @param position position of marker to remove in the arrayList.
     */
    void positionToRemove(int position);
    /**
     * Callback used for fragment ListFragment to communicate with
     * fragment MapFragment. Tells MapFragment which marker has a new
     * markerMessage.
     * @param position position of marker to change marker message
     * @param message the new markerMessage
     */
    void positionToAddMessage(int position, String message);
}
