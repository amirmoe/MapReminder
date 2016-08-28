package com.moemen.android.mapreminder;

import java.util.ArrayList;

/**
 *
 * Interface to allow ListFragment and MapFragment to communicate with each other.
 *
 */
public interface Communicator {
    void arrayToList(ArrayList list);
    void positionToRemove(int i);
}
