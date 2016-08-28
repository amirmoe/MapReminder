package com.moemen.android.mapreminder;


import com.google.android.gms.maps.model.Marker;

/**
 * Represents a marker on the Map. A Marker has built in attributes built from MarkerOptions
 * before becoming an object.
 */
public class MarkerObj {

    private Marker mMarker;
    private String markerMessage;

    /**
     * Change the marker value of the object
     * @param marker marker value.
     */
    public void setMarker(Marker marker){
        this.mMarker = marker;
    }

    /**
     * Gets the marker value
     * @return marker value
     */
    public Marker getMarker(){
        return mMarker;
    }

    /**
     * Gets the current MarkerMessage
     * @return marker message
     */
    public String getMarkerMessage() {
        return markerMessage;
    }

    /**
     * Changes the MarkerMessage
     * @param markerMessage message of the marker.
     */
    public void setMarkerMessage(String markerMessage) {
        this.markerMessage = markerMessage;
    }
}
