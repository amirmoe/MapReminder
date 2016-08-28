package com.moemen.android.mapreminder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * RecyclerView adapter.
 *
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<MarkerObj> markerList;
    private RemoveMarkerPosition mRemoveMarkerPosition;
    private static final String TAG = "FELSÖKNING";

    /**
     * Called upon from the ListFragment when there has been an update to the marker list.
     * @param list Arraylist of markers.
     */
    public void set(ArrayList list){
        markerList = list;
    }

    /**
     * This method is called upon in the listFragment to make it able for the recyclerView and
     * ListFragment to communicate with eachother.
     * @param removeMarkerPosition Initialize communication
     */
    public void setRemoveMarkerPosition(RemoveMarkerPosition removeMarkerPosition) {
        mRemoveMarkerPosition = removeMarkerPosition;
    }

    /**
     * ViewHolder of the RecyclerView. Sets the textView and button for each marker.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public View mView;
        public Button mButton;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.marker_name);
            mButton = (Button) v.findViewById(R.id.remove_button);
            mView = v;
        }
    }

    /**
     * Provide a suitable constructor
     */
    public MyAdapter() {
        markerList = new ArrayList<>();
    }

    /**
     * Creates new views (invoked by the layout manager)
     * @param parent Parent of RecyclerView
     * @param viewType viewType
     * @return view of viewHolder
     */
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_markers, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder viewHolder
     * @param position position in the list.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(markerList.get(position).getMarkerMessage());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRemoveMarkerPosition.onClick(position, markerList);
            }
        });
    }

    /**
     * Return the size of your arrayList (invoked by the layout manager)
     * @return size of arrayList
     */
    @Override
    public int getItemCount() {
        return markerList.size();
    }
}

