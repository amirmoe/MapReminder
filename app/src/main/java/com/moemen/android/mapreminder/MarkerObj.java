package com.moemen.android.mapreminder;


import com.google.android.gms.maps.model.Marker;

/**
 * Created by amir on 2016-08-27.
 */
public class MarkerObj {

    private Marker mMarker;
    private String markerMessage;

    public void setMarker(Marker marker){
        this.mMarker = marker;

    }
    public Marker getMarker(){
        return mMarker;
    }

    public String getMarkerMessage() {
        return markerMessage;
    }

    public void setMarkerMessage(String markerMessage) {
        this.markerMessage = markerMessage;
    }
}
