
package com.moemen.android.mapreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * First view which main purpose is to show a map view
 */
public class MapFragment extends Fragment {

    private static final String TAG = "FELSÖKNING";
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    public static final String KEY_NEXT = "com.moemen.android.mapreminder";
    private BroadcastReceiver receiver;
    private Communicator comm;
    private MapView mMapView;
    private Marker marker;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private NotificationManager mNotificationManager;
    double longitudeGPS, latitudeGPS;
    private int notificationPosition;
    private ArrayList<MarkerObj> markerList = new ArrayList<>();

    /**
     * This method is called upon in the pagerAdapter to make it able for the fragments to communicate
     * with each other
     *
     * @param communicator Initialize the communicator
     */
    public void setCommunicator(Communicator communicator) {
        this.comm = communicator;
    }

    /**
     * This method is called upon in the pagerAdapter when a marker is supposed to be removed from
     * a button press in ListFragment
     * @param pos position of the marker in the array list that is supposed to be removed.
     */
    public void removeMarker(int pos){
        markerList.get(pos).getMarker().remove();
        markerList.remove(pos);
    }

    /**
     * Called upon from the Broadcaster when the user turns of the notification from the notification
     * and not the app.
     */
    public void notificationStop(){
        mNotificationManager.cancel(0);
        removeMarker(notificationPosition);
        comm.arrayToList(markerList);
    }

    /**
     * Called upon after marker has been added. Continuously checks if the coordinates of the users
     * location are close to the coordinates of the marker. if so then it build a notification the
     * alert the user.
     */
    private final LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            longitudeGPS =  location.getLongitude();
            latitudeGPS = location.getLatitude();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Intent nextIntent = new Intent(KEY_NEXT);
                    PendingIntent actionPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, nextIntent, 0);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
                    mBuilder.setSmallIcon(R.drawable.notification_icon);
                    mBuilder.setContentTitle("MapReminder!");
                    //mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                    mBuilder.addAction(R.drawable.ic_close, "Turn off", actionPendingIntent);
                    mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                    for (int i=0; i<markerList.size(); i++){
                        double latitudeMark = markerList.get(i).getMarker().getPosition().latitude;
                        double longitudeMark = markerList.get(i).getMarker().getPosition().longitude;
                        if (latitudeMark-latitudeGPS <= 0.002 && latitudeMark-latitudeGPS >= -0.02){
                            if(longitudeMark-longitudeGPS <= 0.002 && longitudeMark-longitudeGPS >= -0.002){
                                mBuilder.setContentText(markerList.get(i).getMarkerMessage());
                                mNotificationManager.notify(0, mBuilder.build());
                                notificationPosition = i;
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

    /**
     * Initiate the map and mapView.
     * Also initiates a BroadCaster to listen for the notification.
     *
     * @param inflater inflates xml to give a view
     * @param parent Fragments parent ViewGroup
     * @param savedInstanceState Prior state bundle
     * @return view of fragment
     */
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

        IntentFilter filter = new IntentFilter();
        filter.addAction(KEY_NEXT);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(KEY_NEXT)) {
                    notificationStop();
                }
            }
        };
        getActivity().registerReceiver(receiver, filter);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            /**
             * When API has connected, enable user location and then start a listener.
             *
             * @param mMap
             */
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // Set a listener for marker click.
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    /**
                     * When clicking on the map, create a marker that also tries to set the address
                     * of the location as a MarkerMessage. When Marker created, add it to the
                     * arraylist and start the Location listener.
                     * @param point
                     */
                    @Override
                    public void onMapClick(LatLng point) {
                        MarkerOptions options = new MarkerOptions()
                                .position(new LatLng(point.latitude, point.longitude))
                                .title("Unidentified location");
                        marker = googleMap.addMarker(options);

                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                        e.printStackTrace();
                    }
                        String address = "";
                        try {
                            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                        } catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }

                        if (address != null){
                            if (address.length()==0){
                                marker.setTitle("Unknown");
                            } else {
                                marker.setTitle(address);
                            }
                        }

                        MarkerObj tempMarker = new MarkerObj();
                        tempMarker.setMarker(marker);
                        tempMarker.setMarkerMessage(marker.getTitle());
                        markerList.add(tempMarker);
                        comm.arrayToList(markerList);

                        if ( ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                                    MY_PERMISSION_ACCESS_COARSE_LOCATION );
                        }
                        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 2*60*1000, 10, locationListenerGPS); // every minute
                    }
                });
            }
        });
        return v;
    }

}