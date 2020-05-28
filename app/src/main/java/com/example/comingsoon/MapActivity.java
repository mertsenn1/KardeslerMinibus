package com.example.comingsoon;

import  androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.maps.android.PolyUtil;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.comingsoon.App.CHANNEL_ID;
import static com.example.comingsoon.MapActivity.Utils.readEncodedPolyLinePointsFromCSV;
import static com.example.comingsoon.NotificationSettingsActivity.KEY_NOTIFICATION_TIME;
import static com.example.comingsoon.NotificationSettingsActivity.SHARED_PREFERENCES;

/**
 * This is Passenger Map Activity class, when user press the "passenger login" button, this activity starts.
 * Most of the activites that passenger can do is in this class.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    // Properties
    private static final String TAG = MainActivity.class.getSimpleName();

    private final int MIN_TIME = 0;
    private final int MIN_DISTANCE = 1;
    private final int ZOOM = 15;
    private final String GOOGLE_PLACES_API_KEY = "AIzaSyDfSVFl9DV48NyaEKy7Kx-2Orqijf8BCrU";
    private final String[] ROUTES = { "KIZILAY - DİKMEN - ATATÜRK SİTESİ", "ULUS - BALGAT - 100. YIL - ÇİĞDEM",
            "TUNUS - BİLKENT", "SIHHİYE - BİLKENT - AŞTİ", "DENİZCİLER - SIHHİYE - KORU SİTESİ"};


    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private BottomNavigationView bottomNav;
    private LatLng userLocation;
    private LatLng pointFrom;
    private LatLng pointTo;
    private boolean zoomToUser;
    private Marker markerLongClickFrom;
    private List<AutocompletePrediction> predictionList;
    private Marker markerLongClickTo;
    private Marker markerSearch;
    private Marker markerUser;
    private boolean isRouteSelected;
    private boolean notificationSent;
    private BottomSheetDialog routeOptionsDialog;
    private RecyclerView recyclerView;
    private PlacesClient placesClient;
    private MaterialSearchBar materialSearchBar;
    private Location lastKnownLocation;
    private NotificationManagerCompat notificationManagerCompat;

    // Methods
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_map);

        SupportMapFragment mapFragment = ( SupportMapFragment ) getSupportFragmentManager()
                .findFragmentById( R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync( this);

        materialSearchBar = findViewById( R.id.search_bar);

        bottomNav = findViewById( R.id.bottom_navigation);
        bottomNav.setSelectedItemId( R.id.map);
        zoomToUser = true;
        notificationSent = false;

        notificationManagerCompat = NotificationManagerCompat.from( this);

        configureMyLocationButton();

        // bottom navigation fragment buttons.
        bottomNav.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item) {
                switch ( item.getItemId() ) {
                    case R.id.search:
                        startActivity( new Intent( getApplicationContext(), SearchTab.class));
                        return true;
                    case R.id.map:
                        return true;
                    case R.id.settings:
                        startActivity( new Intent( getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                }

                return false;
            }
        });

        // initializing the places and using google api key.
        Places.initialize( MapActivity.this, GOOGLE_PLACES_API_KEY);
        placesClient = Places.createClient( MapActivity.this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        materialSearchBar.setOnSearchActionListener( new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged( boolean enabled) {

            }

            @Override
            public void onSearchConfirmed( CharSequence text) {
                materialSearchBar.setText( "");
            }

            @Override
            public void onButtonClicked( int buttonCode) {
                if ( buttonCode == materialSearchBar.BUTTON_BACK ) {
                    materialSearchBar.closeSearch();
                }
            }
        });

        // apply changes on search bar functions.
        materialSearchBar.addTextChangeListener( new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after) {

            }

            // Ankara From Southwest to Northeast
            RectangularBounds bounds = RectangularBounds.newInstance(
                    new LatLng(39.5, 32.5),
                    new LatLng(40.5, 33.5)
            );

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setCountry( "TR")
                        .setLocationBias( bounds)
                        .setTypeFilter( TypeFilter.ESTABLISHMENT)
                        .setSessionToken( token)
                        .setQuery( s.toString())
                        .build();

                placesClient.findAutocompletePredictions( predictionsRequest).addOnCompleteListener(
                        new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete( @NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if ( task.isSuccessful() ) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();

                            if ( predictionsResponse != null ) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();

                                for ( int i = 0; i < predictionList.size(); i++ ) {
                                    AutocompletePrediction prediction = predictionList.get( i);
                                    suggestionsList.add( prediction.getFullText(null).toString());
                                }

                                materialSearchBar.updateLastSuggestions(suggestionsList);

                                if ( !materialSearchBar.isSuggestionsVisible() ) {
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i (TAG, "prediction fetching task unsuccessful");
                        }
                    }
                });

                if ( materialSearchBar.getText().equals( "") ) {
                    InputMethodManager imm = ( InputMethodManager ) getSystemService( INPUT_METHOD_SERVICE);

                    if ( imm != null ) {
                        imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), 0);
                    }

                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            if ( materialSearchBar.isSuggestionsVisible() ) {
                                materialSearchBar.clearSuggestions();
                                materialSearchBar.setText( "");
                            }
                        }
                    }, 1000);
                }

            }

            @Override
            public void afterTextChanged( Editable s) {

            }
        });

        // apply changes on search bar functions.
        materialSearchBar.setSuggestionsClickListener( new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener( int position, View v) {
                if ( position < predictionList.size() ) {
                    AutocompletePrediction selectedPrediction = predictionList.get( position);
                    String suggestion = materialSearchBar.getLastSuggestions().get( position).toString();

                    materialSearchBar.setText( suggestion);
                    new Handler().postDelayed( () -> {
                        if ( materialSearchBar.isSuggestionsVisible() ) {
                            materialSearchBar.clearSuggestions();
                            materialSearchBar.setText( "");
                        }
                    }, 1000);

                    InputMethodManager imm = ( InputMethodManager ) getSystemService( INPUT_METHOD_SERVICE);
                    if ( imm != null ) {
                        imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }

                    String placeId = selectedPrediction.getPlaceId();
                    List<Place.Field> placeFields = Arrays.asList( Place.Field.LAT_LNG);

                    FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder( placeId, placeFields).build();

                    placesClient.fetchPlace( fetchPlaceRequest).addOnSuccessListener( fetchPlaceResponse -> {
                        Place place = fetchPlaceResponse.getPlace();
                        Log.i( TAG, "onSuccess: Place found " + place.getName());
                        LatLng latLngOfPlace = place.getLatLng();

                        if ( latLngOfPlace != null ) {
                            if ( markerSearch != null) {
                                markerSearch.remove();
                            }
                            moveCameraAndAddMarker( latLngOfPlace, ZOOM, place.getAddress());
                            InputMethodManager im = ( InputMethodManager ) getSystemService( INPUT_METHOD_SERVICE);
                            Log.d( "findRoutes", "checkpoint");
                            try {
                                displayRouteOptions( userLocation, latLngOfPlace);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d( "findRoutes", "userLocation: " + userLocation.toString() + " target: " + latLngOfPlace.toString());
                            routeOptionsDialog.show();

                            if ( im != null ) {
                                im.hideSoftInputFromWindow( materialSearchBar.getWindowToken(), 0);
                            }
                        }
                    }).addOnFailureListener( e -> {
                        if ( e instanceof ApiException ) {
                            ApiException apiException = ( ApiException)  e;
                            apiException.printStackTrace();

                            int statusCode = apiException.getStatusCode();
                            Log.i( TAG, "onFailure: place not found:" + e.getMessage());
                            Log.i( TAG, "onFailure: status code" + statusCode);
                        }
                    });
                }
            }

            @Override
            public void OnItemDeleteListener( int position, View v) {

            }
        });

    }

    /**
     * This method changes the maps ratio and location.
     */
    public void moveCameraAndAddMarker( LatLng latLng, float zoom, String title) {
        Log.d( TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " +
                latLng.longitude);
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latLng, zoom));
        MarkerOptions options = new MarkerOptions().position( latLng).icon( BitmapDescriptorFactory
                .fromResource( R.drawable.ic_place_24px)).title( title);
        markerSearch = mMap.addMarker( options);
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

        if ( lastKnownLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( new LatLng( lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude()), ZOOM));
            zoomToUser = true;
        }

        mMap.setOnMapLongClickListener( new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick( LatLng latLng) {
                if ( markerLongClickFrom != null && markerLongClickTo != null ) {
                    markerLongClickFrom.remove();
                    markerLongClickTo.remove();
                } else if ( markerLongClickFrom == null ) {
                    markerLongClickFrom = mMap.addMarker( new MarkerOptions().position( latLng)
                            .icon( BitmapDescriptorFactory.fromResource( R.drawable.ic_place_24px)));
                    pointFrom = latLng;
                } else if ( markerLongClickTo == null ) {
                    markerLongClickTo = mMap.addMarker( new MarkerOptions().position( latLng)
                            .icon( BitmapDescriptorFactory.fromResource( R.drawable.ic_place_24px)));
                    pointTo = latLng;
                    try {
                        displayRouteOptions( pointFrom, pointTo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        locationManager = ( LocationManager ) this.getSystemService( Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged( Location location) {
                LatLng passengerLocation = new LatLng( location.getLatitude(), location.getLongitude());

                FirebaseDatabase.getInstance().getReference().child( "Passengers")
                        .child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child( "location").setValue( passengerLocation);

                //  Add a marker in the location of the user and move the camera
                userLocation = new LatLng( location.getLatitude(), location.getLongitude());

                Intent intent = getIntent();
                int route = intent.getIntExtra("route", -1);

                drawPolyline( route, mMap);
                markerUser.setPosition( userLocation);
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
            if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        } else {
            if ( ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            } else {
                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
                lastKnownLocation = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER);

                assert lastKnownLocation != null;
                LatLng userLocation = new LatLng( lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                markerUser = mMap.addMarker(new MarkerOptions().position(userLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_24px)));
                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( userLocation, ZOOM));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        if ( requestCode == 1 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        }
    }

    /**
     * Method to draw all polylines. This will manually draw polylines one by one on the map by calling
     * addPolyline(PolylineOptions) on a map instance. The parameter passed in is a new PolylineOptions
     * object which can be configured with details such as line color, line width, clickability, and
     * a list of coordinates values.
     */
    protected void drawPolyline( int route, GoogleMap map) {
        FirebaseDatabase.getInstance().getReference().child( "Passengers")
                .child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child( "route").setValue( route);

        isRouteSelected = true;

        // Draw the desired polyline.
        switch ( route ) {
            case 1:
                drawPolylineHelper( "route1");
                zoomToUser = false;
                displayBusesOnRoute( 1);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng(39.881531, 32.839319), 13));
                break;
            case 2:
                drawPolylineHelper( "route2");
                zoomToUser = false;
                displayBusesOnRoute( 2);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng(39.917185, 32.825929), 13));
                break;
            case 3:
                drawPolylineHelper( "route3");
                zoomToUser = false;
                displayBusesOnRoute( 3);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng(39.910318, 32.804689), 12));
                break;
            case 4:
                drawPolylineHelper( "route4");
                zoomToUser = false;
                displayBusesOnRoute( 4);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng(39.908998, 32.796110), 11));
                break;
            case 5:
                drawPolylineHelper( "route5");
                zoomToUser = false;
                displayBusesOnRoute( 5);
                map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng(39.908425, 32.776701), 11));
                break;
        }
    }

    /**
     * A helper method for drawing polylines.
     */
    protected void drawPolylineHelper( String lineKeyword) {
        mMap.addPolyline( new PolylineOptions()
                .color(getResources().getColor( R.color.highlight)) // Line color.
                .width( 10) // Line width.
                .clickable( false) // Able to click or not.
                .addAll( Utils.readEncodedPolyLinePointsFromCSV( this, lineKeyword)));
    }

    /**
     * This method adds markers to for the buses that selected by passenger.
     * Updates itself whenever a data changed on database.
     */
    private void displayBusesOnRoute( final long route) {
        final ArrayList<Marker> busMarkers = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child( "Drivers")
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        if ( !busMarkers.isEmpty() ) {
                            for ( Marker marker: busMarkers ) {
                                marker.remove();
                            }
                        }
                        for ( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                            long driverRoute = ( long ) snapshot.child( "route").getValue();

                            if ( driverRoute == route ) {
                                HashMap<String, Double> driverLocation = ( HashMap<String, Double> ) snapshot.child( "location").getValue();
                                double lat = Double.parseDouble( driverLocation.get( "latitude").toString());
                                double lng = Double.parseDouble( driverLocation.get( "longitude").toString());

                                LatLng location = new LatLng( lat, lng);

                                busMarkers.add( mMap.addMarker( new MarkerOptions().position( location).icon( BitmapDescriptorFactory
                                        .fromResource( R.drawable.bus_middle))));

                                checkDuration( location, userLocation);
                            }
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {
                    }
                });
    }


    // Inner Classes
    /**
     * This class is to make polyline issues easier.
     */
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
                    if ( tokens[0].trim().equals( lineKeyword) && tokens[1].trim().equals( ENCODED_POINTS) ) {

                        // Use PolyUtil to decode the polylines path into list of LatLng objects.
                        latLngList.addAll( PolyUtil.decode( tokens[2].trim().replace( "\\\\", "\\")));
                        Log.d(LOG_TAG + lineKeyword, tokens[2].trim());

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

    /**
     * This method simply changes the map's view to your location
     */
    public void configureMyLocationButton() {
        FloatingActionButton myLocationButton = findViewById( R.id.floatingActionButton);
        myLocationButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                mMap.animateCamera( CameraUpdateFactory.newLatLngZoom( userLocation, ZOOM));
                zoomToUser = true;

                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        if( materialSearchBar.isSuggestionsVisible() ) {
                            materialSearchBar.clearSuggestions();
                            materialSearchBar.setText( "");
                        }
                    }
                }, 1000);

            }
        });
    }

    /**
     * This method looks the points you choose and the proper routes for them.
     * @param from , where you want go from
     * @param to, where you want to go
     * @return foundRoutes , which is an arrayList contains routes that contains the points passenger choose.
     */
    private ArrayList<String> findRoute( LatLng from, LatLng to) {
        ArrayList<String> foundRoutes = new ArrayList<>();

        for ( String route : ROUTES ) {
            String routeCode = "route" + Arrays.asList( ROUTES).indexOf( route);

            List<LatLng> polyline = readEncodedPolyLinePointsFromCSV( this, routeCode);

            boolean containsTo = PolyUtil.isLocationOnPath( to, polyline, false, 500);
            boolean containsFrom = PolyUtil.isLocationOnPath( from, polyline, false, 500);

            if ( containsFrom && containsTo)  {
                foundRoutes.add( route);
                Log.d( "findRoutes - foundRoutes", "route: " + route);
            }
        }

        return foundRoutes;
    }


    /**
     * This method finds the routes between two points and displays it
     * @param from , where you want go from
     * @param to, where you want to go
     */
    private void displayRouteOptions( LatLng from, LatLng to) throws InterruptedException, ExecutionException, JSONException, IOException {

        routeOptionsDialog = new BottomSheetDialog( MapActivity.this);
        View routeOptionsDialogView = getLayoutInflater().inflate( R.layout.route_options_sheet, null);
        routeOptionsDialog.setContentView( routeOptionsDialogView);
        recyclerView = routeOptionsDialog.findViewById( R.id.recyclerView);

        ArrayList<String> foundRoutes = new ArrayList<>();
        ArrayList<String> durations = new ArrayList<>();

        if ( from != null && to != null ) {
            foundRoutes = findRoute( from, to);
        }

        ArrayList<String> finalFoundRoutes = foundRoutes;
        ArrayList<String> routesForAdapter = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child( "Drivers")
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        for ( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                            long driverRoute = (long) snapshot.child("route").getValue();

                                if (finalFoundRoutes.contains(ROUTES[(int) driverRoute - 1])) {
                                    routesForAdapter.add( ROUTES[(int) driverRoute - 1]);
                                    HashMap<String, Double> driverLocation = (HashMap<String, Double>) snapshot.child("location").getValue();
                                    double lat = Double.parseDouble(driverLocation.get("latitude").toString());
                                    double lng = Double.parseDouble(driverLocation.get("longitude").toString());

                                    LatLng location = new LatLng(lat, lng);

                                    try {
                                        durations.add(getDurationBetweenPoints(location, userLocation));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        if ( durations.size() < routesForAdapter.size()) {
                            while( durations.size() == routesForAdapter.size() ){
                                durations.add( "No driver available on this route");
                            }
                        }

                        FoundRouteAdapter adapter = new FoundRouteAdapter( routeOptionsDialog.getContext(),
                                routesForAdapter, durations, R.drawable.sheet_icon, MapActivity.this);

                        recyclerView.setAdapter( adapter);
                        recyclerView.setLayoutManager( new LinearLayoutManager( MapActivity.this));
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {
                    }
                });

    }


    public void routeOptionsOnClick( int position, ArrayList<String> arr) {
        String str = arr.get( position);
        int index = Arrays.asList( ROUTES).indexOf( str);

        routeOptionsDialog.dismiss();
        Log.d("Draw Polyline", "routeOptionOnClick: dismissed");
        drawPolyline( index + 1, mMap);
    }

    /**
     * This method gets the request URL generated in order to find the direction information
     * @param origin, destination two locations to
     * @return url giving direction information
     */
    private String getDirectionsUrl(LatLng origin,LatLng dest){
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Project's Google map api key
        String key = "key=" + "AIzaSyDfSVFl9DV48NyaEKy7Kx-2Orqijf8BCrU";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"+ key;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    // inner class
    /**
     * This class gets the Http request and downloads the url in background.
     */
    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        private static final String REQUEST_METHOD = "GET";
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEOUT = 15000;

        /**
         * This method gets the response from given URL.
         */
        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                // Create a URL object holding our url
                URL myUrl = new URL(stringUrl);

                // Create a connection
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();

                // Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                // Connect to our url
                connection.connect();

                // Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

                // Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                // Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }

                // Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();

                // Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }

    /**
     * This method gets the duration between given points
     * @param origin, destination two points to find duration between
     * @return duration in minutes
     */
    private String getDurationBetweenPoints( LatLng origin, LatLng destination) throws IOException, JSONException, ExecutionException, InterruptedException {

        String response;
        String url;
        String duration;
        JSONObject jsonObject, route, durationObject, leg;
        JSONArray routesArray, legs;

        url = getDirectionsUrl( origin, destination);
        // https://maps.googleapis.com/maps/api/directions/json?origin=39.880759,32.754736&destination=39.905854,32.764763&sensor=false&key=AIzaSyDfSVFl9DV48NyaEKy7Kx-2Orqijf8BCrU
        response = new HttpGetRequest().execute( url).get();

        jsonObject = new JSONObject( response);

        // routesArray contains ALL routes
        routesArray = jsonObject.getJSONArray("routes");

        // Grab the first route
        route = routesArray.getJSONObject(0);

        // Take all legs from the route
        legs = route.getJSONArray("legs");

        // Grab first leg
        leg = legs.getJSONObject(0);

        durationObject = leg.getJSONObject("duration");
        duration = durationObject.getString("text");

        return duration;

    }

    /**
     * This method sends notification to the passenger if the bus is close.
     * @param duration is left travel time of bus
     */
    public void sendNotification( String duration){
        Notification notification = new NotificationCompat.Builder( this, CHANNEL_ID)
                .setSmallIcon( R.drawable.path)
                .setContentTitle( "Coming Soon")
                .setContentText( "Your bus will arrive in " + duration + " minute")
                .setPriority( NotificationCompat.PRIORITY_HIGH)
                .setCategory( NotificationCompat.CATEGORY_REMINDER).build();

        notificationManagerCompat.notify ( 1, notification);
    }

    /**
     * This method gets the set notification time
     * @return notificationTime
     */
    public int setNotificationTime() {
        SharedPreferences preferences = getSharedPreferences( SHARED_PREFERENCES, MODE_PRIVATE);
        int notificationTime = preferences.getInt( KEY_NOTIFICATION_TIME, 300);
        Log.d( "NOTIFICATION", "" + notificationTime);

        return notificationTime;
    }

    /**
     * This method checks the duration to get the information of whether the notification may sent or not.
     * @param bus, user two points to get the duration
     */
    public void checkDuration( LatLng bus, LatLng user) {
        String s;
        s = "";

        try {
            s = getDurationBetweenPoints( bus, user);
            Log.d( "DURATION TEST", s);
        } catch (IOException e) {
            Log.d( "DURATION TEST", "IO EXCEPTION");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d( "DURATION TEST", "JSON EXCEPTION");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d( "DURATION TEST", "INTERRUPTED EXCEPTION");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d( "DURATION TEST", "EXECUTION EXCEPTION");
            e.printStackTrace();
        }

        if ( !s.equals("")) {
            s = s.replace( " mins", "");
            s = s.replace( " min", "");
            int duration = Integer.parseInt( s);
            duration = duration * 60;

            if ( duration <= setNotificationTime() && !notificationSent) {
                sendNotification( s);
                notificationSent = true;
            }
        }
    }
}
