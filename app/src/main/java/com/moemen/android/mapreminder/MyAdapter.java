package com.moemen.android.mapreminder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * RecyclerView adapter.
 *
 */
class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<MarkerObj> markerList;
    private ListFragmentCommunication mListFragmentCommunication;
    /**
     * Called upon from the ListFragment when there has been an update to the marker list.
     * @param list ArrayList of markers.
     */
    @SuppressWarnings("unchecked")
    void set(ArrayList list){
        markerList = list;
    }

    /**
     * This method is called upon in the listFragment to make it able for the recyclerView and
     * ListFragment to communicate with each other.
     * @param listFragmentCommunication Initialize communication
     */
    void setListFragmentCommunication(ListFragmentCommunication listFragmentCommunication) {
        mListFragmentCommunication = listFragmentCommunication;
    }

    /**
     * ViewHolder of the RecyclerView. Sets the textView and button for each marker.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        View mView;
        Button mRemoveButton;
        Button mMessageButton;
        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.marker_name);
            mRemoveButton = (Button) v.findViewById(R.id.remove_button);
            mMessageButton = (Button) v.findViewById(R.id.messageButton);
            mView = v;
        }
    }

    /**
     * Provide a suitable constructor
     */
    MyAdapter() {
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
        return new ViewHolder(v);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder viewHolder
     * @param position position in the list.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(markerList.get(position).getMarkerMessage());
        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When removeButton is pressed, send the position of the marker to be removed to
             * MapFragment
             * @param view view
             */
            @Override
            public void onClick(View view) {
                mListFragmentCommunication.removeMarkerPosition(position, markerList);
            }
        });
        holder.mMessageButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When messageButton is pressed, a Dialog interface pops up and lets the user
             * write in the new marker message.
             * @param view view
             */
            @Override
            public void onClick(final View view) {
                View v = (LayoutInflater.from(view.getContext())).inflate(R.layout.user_input, null);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                alertBuilder.setView(v);
                final EditText userInput =  (EditText) v.findViewById(R.id.userInput);

                alertBuilder.setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(view.getContext(), "Added \"" + userInput.getText().toString() + "\" as a message in the notification", Toast.LENGTH_SHORT).show();
                                mListFragmentCommunication.addMessageMarkerPosition(position, userInput.getText().toString());
                            }
                        });
                Dialog dialog = alertBuilder.create();
                dialog.show();
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

