package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is activity class that enables user to search different routes.
 */
public class SearchTab extends AppCompatActivity {
    //Constants
    private final ArrayList<String> ROUTES = new ArrayList<String>(Arrays.asList("KIZILAY - DİKMEN - ATATÜRK SİTESİ", "ULUS - BALGAT - 100. YIL - ÇİĞDEM",
            "TUNUS - BİLKENT", "SIHHİYE - BİLKENT - AŞTİ", "DENİZCİLER - SIHHİYE - KORU SİTESİ"));

    // Properties
    private BottomNavigationView bottomNav;
    private RecyclerView allRoutes;
    private Bundle bundle;
    private FilterAdapter adapter;
    private SearchView searchView;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_search_tab);

        bottomNav = findViewById( R.id.bottom_navigation);
        bottomNav.setSelectedItemId( R.id.search);

        /**
         * Setting up the navigation in bottom navbar.
         */
        bottomNav.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item) {
                switch ( item.getItemId() ) {
                    case R.id.search:
                        return true;
                    case R.id.map:
                        startActivity( new Intent( getApplicationContext(), MapActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.settings:
                        startActivity( new Intent( getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                }

                return false;
            }
        });

        setUpRoutesRecyclerView();
        configureSearchView();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnFocusChangeListener( new FocusListener());
    }

    private void setUpRoutesRecyclerView() {

        allRoutes = findViewById( R.id.search_all_routes);

        adapter = new FilterAdapter(SearchTab.this, ROUTES, R.drawable.searchbusicon,SearchTab.this);

        allRoutes.setAdapter( adapter);
        allRoutes.setLayoutManager( new LinearLayoutManager( SearchTab.this));
    }

    private void configureSearchView() {
        searchView = (SearchView) findViewById( R.id.route_filter_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter( newText);
                return false;
            }
        });
    }

    class FocusListener implements View.OnFocusChangeListener {

        // Constructors
        public FocusListener() {

        }

        // Methods
        @Override
        public void onFocusChange (View v, boolean hasFocus) {
            if ( !hasFocus ) {
                hideKeyboard( v);
            }
        }
    }

    public void hideKeyboard( View view) {
        InputMethodManager inputMethodManager = ( InputMethodManager ) getSystemService( Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow( view.getWindowToken(), 0);
    }

    public int findRouteId( String str) {
        int i = ROUTES.indexOf( str) + 1;
        return i;
    }
}
