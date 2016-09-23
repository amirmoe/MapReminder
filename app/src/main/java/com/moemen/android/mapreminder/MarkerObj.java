package com.moemen.android.mapreminder;


import com.google.android.gms.maps.model.Marker;

/**
 * Represents a marker on the Map. A Marker has built in attributes built from MarkerOptions
 * before becoming an object.
 */
class MarkerObj {

    private Marker mMarker;
    private String markerMessage;

    /**
     * Change the marker value of the object
     * @param marker marker value.
     */
    void setMarker(Marker marker){
        this.mMarker = marker;
    }

    /**
     * Gets the marker value
     * @return marker value
     */
    Marker getMarker(){
        return mMarker;
    }

    /**
     * Gets the current MarkerMessage
     * @return marker message
     */
    String getMarkerMessage() {
        return markerMessage;
    }

    /**
     * Changes the MarkerMessage
     * @param markerMessage message of the marker.
     */
    void setMarkerMessage(String markerMessage) {
        this.markerMessage = markerMessage;
    }
}
