package com.moemen.android.mapreminder;

/**
 * Created by amir on 2016-08-21.
 *
 * Interface to allow automatic tab switch when finishing the game and restarting
 */
public interface TabSelector {
    void onTabSwitch(int tabPosition);
}
