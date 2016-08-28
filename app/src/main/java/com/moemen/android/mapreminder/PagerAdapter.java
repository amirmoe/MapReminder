package com.moemen.android.mapreminder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;


/**
 * Adapter to populate pages inside our ViewPager
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private MapFragment mMapFragment;
    private ListFragment mListFragment;
    /**
     * Initialize the PagerAdapter and also create new fragment-objects.
     *
     * @param fm fragmentManager to keep track of fragments
     * @param NumOfTabs number of tabs/views.
     */
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.mMapFragment = new MapFragment();
        this.mListFragment = new ListFragment();
    }
    /**
     * Takes the position of the view we want to change to and returns corresponding fragment
     *
     * @param position of the view
     * @return fragment of the new view.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MapFragment tab1 = this.mMapFragment  ;
                tab1.setCommunicator(new Communicator() {
                    /**
                     * Callback used for fragment MapFragment to communicate with
                     * fragment ListFragment. Updates the arraylist with markers.
                     * @param list update of the markers
                     */
                    @Override
                    public void arrayToList(ArrayList list) {
                        ListFragment frag = (ListFragment) getItem(1);
                        frag.sendArray(list);
                    }
                    @Override
                    public void positionToRemove(int i) {}
                });
                return tab1;

            case 1:
                ListFragment tab2 = this.mListFragment;
                tab2.setCommunicator(new Communicator() {
                    @Override
                    public void arrayToList(ArrayList list) {}
                    /**
                     * Callback used for fragment ListFragment to communicate with
                     * fragment MapFragment. Tells MapFragment which marker to remove
                     * @param i position of marker to remove in the arraylist.
                     */
                    @Override
                    public void positionToRemove(int i) {
                        MapFragment frag = (MapFragment) getItem(0);
                        frag.removeMarker(i);
                    }
                });
                return tab2;
            default:
                return null;
        }
    }

    /**
     * Method that returns the number of tabs
     * @return number of tabs
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}