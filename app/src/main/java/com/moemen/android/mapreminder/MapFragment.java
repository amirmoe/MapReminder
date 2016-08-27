
package com.moemen.android.mapreminder;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.location.LocationListener;

//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by amir on 2016-08-21.
 */
public class MapFragment extends Fragment {

    private static final String TAG = "FELSÖKNING";
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

    private MapView mMapView;
    private Marker marker;
    private GoogleMap googleMap;
    private TabSelector mTabSelector;
    private LocationManager locationManager;
    double longitudeGPS, latitudeGPS;
    private ArrayList<MarkerObj> markerList = new ArrayList<>();


/*
    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showAlert() {
        if (!isLocationEnabled()) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Enable Location")
                    .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                            "use this app")
                    .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
            dialog.show();
        }
    }
*/
        /*private void CheckOldMarker(LatLng point){
        for (int i=0; i<markerList.size();i++){
            Toast.makeText(getActivity(),point.latitude+" : "+point.longitude, Toast.LENGTH_SHORT).show();
            if (point.latitude == marker.getPosition().latitude && point.longitude == marker.getPosition().latitude){
                markerList.remove(marker);
                marker.remove();
            }
        }
    }*/


    /**
     * This method is called upon in the pagerAdapter to switch tab view after all 10 rounds
     *
     * @param tabSelector Initialize the tabSelector
     */
    public void setTabSelector(TabSelector tabSelector) {
        mTabSelector = tabSelector;
    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            longitudeGPS =  location.getLongitude();
            latitudeGPS = location.getLatitude();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getActivity(),latitudeGPS+" : "+longitudeGPS, Toast.LENGTH_SHORT).show();

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
                    mBuilder.setSmallIcon(R.drawable.notification_icon);
                    mBuilder.setContentTitle("MapReminder!");
                    NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                    for (int i=0; i<markerList.size(); i++){
                        double latitudeMark = markerList.get(i).getMarker().getPosition().latitude;
                        double longitudeMark = markerList.get(i).getMarker().getPosition().longitude;
                        if (latitudeMark-latitudeGPS <= 0.002 && latitudeMark-latitudeGPS >= -0.02){
                            if(longitudeMark-longitudeGPS <= 0.002 && longitudeMark-longitudeGPS >= -0.002){
                                mBuilder.setContentText(markerList.get(i).getMarkerMessage());
                                mNotificationManager.notify(0, mBuilder.build());
                            }
                        }
                    }
                }
            });

        }
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            // TODO Auto-generated method stub
        }
    };




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, parent, false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                //showAlert();

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);
                /*double lat =  latitudeGPS;
                double lng = longitudeGPS;
                LatLng zoomCoordinate = new LatLng(lat, lng);*/


                // For dropping a marker at a point on the Map
                /*LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));*/

                // For zooming automatically to the location of the marker
             /*   CameraPosition cameraPosition = new CameraPosition.Builder().target(zoomCoordinate).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

                // Set a listener for marker click.
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        MarkerOptions options = new MarkerOptions()
                                .position(new LatLng(point.latitude, point.longitude))
                                .title("New Marker");
                        marker = googleMap.addMarker(options);
                        MarkerObj tempMarker = new MarkerObj();
                        tempMarker.setMarker(marker);
                        tempMarker.setMarkerMessage(marker.getTitle());
                        markerList.add(tempMarker);



                        if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                                    MY_PERMISSION_ACCESS_COARSE_LOCATION );
                        }
                        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 1000, 0, locationListenerGPS); // 2*60*1000, 10
                    }
                });
            }
        });
        return v;
    }
}