
package com.moemen.android.mapreminder;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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
public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks{

    private static final String TAG = "FELSÖKNING";
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

    MapView mMapView;
    Marker marker;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private TabSelector mTabSelector;
    LocationManager locationManager;
    double longitudeGPS, latitudeGPS;

    /*private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }*/

    private void showAlert() {
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

    /*private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }*/


    /**
     * This method is called upon in the pagerAdapter to switch tab view after all 10 rounds
     *
     * @param tabSelector Initialize the tabSelector
     */
    public void setTabSelector(TabSelector tabSelector) {
        mTabSelector = tabSelector;
    }

    private void CheckOldMarker(LatLng point){
        for (int i=0; i<mMarkerArray.size();i++){
            Toast.makeText(getActivity(),point.latitude+" : "+point.longitude, Toast.LENGTH_SHORT).show();
            if (point.latitude == marker.getPosition().latitude && point.longitude == marker.getPosition().latitude){
                mMarkerArray.remove(marker);
                marker.remove();
            }
        }
    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitudeGPS =  location.getLongitude();
            latitudeGPS = location.getLatitude();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(),latitudeGPS+" : "+longitudeGPS, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle){

        }

        @Override
        public void onProviderEnabled(String s){

        }

        @Override
        public void onProviderDisabled(String s){

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

                /*if(!checkLocation()){
                    System.out.println("vafan");
                }*/

                //showAlert();

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);


                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                // Set a listener for marker click.

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        CheckOldMarker(point);

                        MarkerOptions options = new MarkerOptions()
                                .position(new LatLng(point.latitude, point.longitude))
                                .title("New Marker");
                        marker = googleMap.addMarker(options);
                        System.out.println(point.latitude + "---" + point.longitude);
                        mMarkerArray.add(marker);




                    }
                });

                if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                    ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                            MY_PERMISSION_ACCESS_COARSE_LOCATION );
                }

                locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // TODO Auto-generated method stub
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
                });

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
                mBuilder.setSmallIcon(R.drawable.notification_icon);
                mBuilder.setContentTitle("Notification Alert, Click Me!");
                mBuilder.setContentText("Hi, This is Android Notification Detail!");

                NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(0, mBuilder.build());




            }



        });

        return v;
    }

    @Override
    public void onConnected(Bundle bundle){
        Toast.makeText(getActivity(),"connected", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "tja");

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(1000);
        /*//locationManager.requestLocationUpdates(request,getActivity());
        locationManager.requestLocationUpdates(request,getActivity());

        if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    LocationService.MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,1000,locationListenerGPS);*/
    }

    @Override
    public void onConnectionSuspended(int i){
    }


}