package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is the main driver class.
 * Retrieve and upload information to Firebase.
 * Enables drivers to see the route's status and other buses on the route.
 */

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback {

    // Properties
    private final int MIN_TIME = 0;
    private final int MIN_DISTANCE = 1;
    private final int ZOOM = 15;
    private final float POINT_1 = (float) 0.5;
    private final float POINT_2 = (float) 0.5;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    BottomNavigationView bottomNav;
    SharedPreferences sharedDriverPreferences;
    private long driverRoute;

    // Methods
    @Override
    /**
     * This method checks permissions and if permission granted, calls a function to update location update regularly.
     */
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        if ( requestCode == 1 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        }
    }


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_driver_map);

        bottomNav = findViewById( R.id.driver_bottom_navigation);
        bottomNav.setSelectedItemId( R.id.map_driver);


        /**
         * This method is for bottom navigation and to start its activities.
         */
        bottomNav.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item) {
                switch ( item.getItemId() ) {
                    case R.id.journey:
                        startActivity( new Intent( getApplicationContext(), JourneyInfoActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.map:
                        return true;
                    case R.id.driver_settings:
                        startActivity( new Intent( getApplicationContext(), DriverSettingsActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                }

                return false;
            }
        });

        setDriverRoute();

        // variables
//        PlacesClient placesClient;
//        AutocompleteSupportFragment autocompleteFragment;

        // program code

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ( SupportMapFragment ) getSupportFragmentManager()
                .findFragmentById( R.id.map);
        mapFragment.getMapAsync( this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we initially show the location of the user on the app.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady( GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = ( LocationManager ) this.getSystemService( Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged( Location location) {

                //  Add a marker in the location of the user and move the camera
                LatLng userLocation = new LatLng( location.getLatitude(), location.getLongitude());

                // upload the drivers data to firebase.
                FirebaseDatabase.getInstance().getReference().child( "Drivers")
                        .child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child( "location").setValue( userLocation);
                FirebaseDatabase.getInstance().getReference().child( "Drivers")
                        .child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child( "bearing").setValue( location.getBearing());

                mMap.animateCamera( CameraUpdateFactory.newLatLngZoom( userLocation, ZOOM)); //modify
            }


            @Override
            public void onStatusChanged( String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled( String provider) {

            }

            @Override
            public void onProviderDisabled( String provider) {

            }
        };

        if ( Build.VERSION.SDK_INT < 23 ) {
            if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        } else {
            if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            } else {
                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER);

                // Move the camera to the last known location of the user and move the camera
                assert lastKnownLocation != null;

                LatLng userLocation = new LatLng( lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(userLocation, ZOOM));
            }
        }
    }

    /**
     * This method helps to display the other buses on the route.
     * Updates the map whenever a change happened in firebase database.
     */
    private void displayBusesOnRoute( final long route) {
        final ArrayList<Marker> busMarkers = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child( "Drivers")
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        Log.d( "SAME ROUTE", "displayBusesOnRoute: listening");
                        if ( !busMarkers.isEmpty() ) {
                            Log.d( "SAME ROUTE", "displayBusesOnRoute: deleting old markers");
                            for ( Marker marker: busMarkers ) {
                                marker.remove();
                            }
                        }
                        for ( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                            long checkDriverRoute = ( long ) snapshot.child( "route").getValue();
                            Log.d( "SAME ROUTE", "displayBusesOnRoute: checking" + checkDriverRoute);
                            Log.d( "SAME ROUTE", "displayBusesOnRoute: this driver route: " + route);

                            if ( checkDriverRoute == route ) {
                                Log.d( "SAME ROUTE", "displayBusesOnRoute: same route");
                                HashMap<String, Double> driverLocation = ( HashMap<String, Double> ) snapshot.child( "location").getValue();
                                double lat = Double.parseDouble( driverLocation.get( "latitude").toString());
                                double lng = Double.parseDouble( driverLocation.get( "longitude").toString());

                                LatLng location = new LatLng( lat, lng);

                                busMarkers.add( mMap.addMarker( new MarkerOptions().position( location).icon( BitmapDescriptorFactory
                                        .fromResource( R.drawable.bus))));
                            }
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {

                    }
                });
    }

    /**
     * This method helps to display passengers on a specific route
     */
    private void displayPassengersOnRoute( final long route) {
        final ArrayList<Marker> passengerMarkers = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child( "Passengers")
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        Log.d( "SAME ROUTE", "displayPassengersOnRoute: listening");
                        if ( !passengerMarkers.isEmpty() ) {
                            Log.d( "SAME ROUTE", "displayPassengersOnRoute: deleting old markers");
                            for ( Marker marker: passengerMarkers ) {
                                marker.remove();
                            }
                        }
                        for ( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                            long checkDriverRoute = ( long ) snapshot.child( "route").getValue();
                            Log.d( "SAME ROUTE", "displayPassengersOnRoute: checking passenger " + checkDriverRoute);
                            Log.d( "SAME ROUTE", "displayPassengersOnRoute: this driver route: " + route);

                            if ( checkDriverRoute != -1 && checkDriverRoute == route ) {
                                Log.d(  "SAME ROUTE", "displayPassengersOnRoute: same route");
                                HashMap<String, Double> passengerLocation = ( HashMap<String, Double> ) snapshot.child( "location").getValue();
                                double lat = Double.parseDouble( passengerLocation.get( "latitude").toString());
                                double lng = Double.parseDouble( passengerLocation.get( "longitude").toString());

                                LatLng location = new LatLng( lat, lng);

                                passengerMarkers.add( mMap.addMarker( new MarkerOptions().position( location).icon( BitmapDescriptorFactory
                                        .fromResource( R.drawable.ic_place_24px))));
                            }
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {

                    }
                });
    }

    /**
     * This method changes the drivers route when the driver change him route.
     */
    private void setDriverRoute() {
        FirebaseDatabase.getInstance().getReference().child( "Drivers").child( FirebaseAuth
                .getInstance().getCurrentUser().getUid()).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot) {
                driverRoute = ( long ) dataSnapshot.child("route").getValue();
                drawPolyline( ( int ) driverRoute, mMap);
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError) {

            }
        });

        drawPolyline( ( int ) driverRoute, mMap);
    }

    // Inner Classes
    public static class Utils {

        // Properties
        private static final String LOG_TAG = MapActivity.class.getName();
        public static final String ENCODED_POINTS = "encodedPoints";

        // Methods

        /**
         * Helper method to get polyline points by decoding an encoded coordinates string read from CSV file.
         */
        static List<LatLng> readEncodedPolyLinePointsFromCSV( Context context, String lineKeyword) {

            // Create an InputStream object.
            InputStream is = context.getResources().openRawResource( R.raw.polylines);

            // Create a BufferedReader object to read values from CSV file.
            BufferedReader reader = new BufferedReader( new InputStreamReader( is, StandardCharsets.UTF_8));
            String line = "";

            // Create a list of LatLng objects.
            List<LatLng> latLngList = new ArrayList<>();
            try {
                while ( ( line = reader.readLine() ) != null ) {

                    // Split the line into different tokens (using the comma as a separator).
                    String[] tokens = line.split( ",");

                    // Only add the right latlng points to a desired line by color.
                    if ( tokens[0].trim().equals( lineKeyword) && tokens[1].trim().equals(ENCODED_POINTS) ) {

                        // Use PolyUtil to decode the polylines path into list of LatLng objects.
                        latLngList.addAll( PolyUtil.decode(tokens[2].trim().replace( "\\\\", "\\")));
                        Log.d( LOG_TAG + lineKeyword, tokens[2].trim());
                        for ( LatLng lat : latLngList ) {
                            Log.d( LOG_TAG + lineKeyword, lat.latitude + ", " + lat.longitude);
                        }
                    } else {
                        Log.d( LOG_TAG, "null");
                    }
                }
            } catch ( IOException e1 ) {
                Log.e( LOG_TAG, "Error" + line, e1);
                e1.printStackTrace();
            }

            return latLngList;
        }
    }

    protected void drawPolylineHelper( String lineKeyword) {
        mMap.addPolyline( new PolylineOptions()
                .color(getResources().getColor( R.color.highlight)) // Line color.
                .width( 10) // Line width.
                .clickable( false) // Able to click or not.
                .addAll( MapActivity.Utils.readEncodedPolyLinePointsFromCSV( this, lineKeyword)));
    }

    protected void drawPolyline( int route, GoogleMap map) {

        //Creating line keyword
        String lineKeyword = "route" + driverRoute;
        Log.d( "SAME ROUTE" , "Line keyword: " + lineKeyword);

        // Draw the desired polyline.
        switch ( route ) {
            case 1:
                drawPolylineHelper("route1");
                displayBusesOnRoute( 1);
                displayPassengersOnRoute( 1);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( 39.881531, 32.839319), 13));
                break;
            case 2:
                drawPolylineHelper("route2");
                displayBusesOnRoute( 2);
                displayPassengersOnRoute( 2);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( 39.917185, 32.825929), 13));
                break;
            case 3:
                drawPolylineHelper("route3");
                displayBusesOnRoute( 3);
                displayPassengersOnRoute( 3);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( 39.910318, 32.804689), 12));
                break;
            case 4:
                drawPolylineHelper("route4");
                displayBusesOnRoute( 4);
                displayPassengersOnRoute( 4);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( 39.908998, 32.796110), 11));
                break;
            case 5:
                drawPolylineHelper("route5");
                displayBusesOnRoute( 5);
                displayPassengersOnRoute( 5);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( 39.908425, 32.776701), 11));
                break;
        }
    }
}
