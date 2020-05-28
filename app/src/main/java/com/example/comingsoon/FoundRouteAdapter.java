package com.example.comingsoon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class is to show the route information to user on the adapter
 */
public class FoundRouteAdapter extends RecyclerView.Adapter<FoundRouteAdapter.ViewHolder>{

    // Properties
    private static final String TAG = "RecyclerViewAdapter";

    MapActivity mapActivity;
    private ArrayList<String> routes;
    private ArrayList<String> durations;
    private Context context;
    private int busIcon;

    // Constructors
    public FoundRouteAdapter( Context context, ArrayList<String> routes, ArrayList<String> durations, int busIcon, MapActivity mapActivity) {
        this.context = context;
        this.routes = routes;
        this.durations = durations;
        this.busIcon = busIcon;
        this.mapActivity = mapActivity;
    }

    // Methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.my_row, parent, false);
        RecyclerView.ViewHolder holder= new ViewHolder( view);
        return ( ViewHolder ) holder;
    }

    @Override
    /**
     * This method sets the information of route
     */
    public void onBindViewHolder( @NonNull ViewHolder holder, final int position) {
        Log.d( TAG, "onBindViewHolder: called");
        holder.route.setText( routes.get( position));
        holder.duration.setText( durations.get( position));
        holder.icon.setImageResource( busIcon);

        holder.linearLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                Log.d( TAG, "onBindViewHolder: onClick");
                Log.d( TAG, routes.get( position));
                mapActivity.routeOptionsOnClick( position, routes);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    // inner class
    /**
     * This class is to bind layout.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView route;
        TextView duration;
        LinearLayout linearLayout;
        ImageView icon;

        public ViewHolder( @NonNull View itemView) {
            super( itemView);
            route = itemView.findViewById( R.id.optionsRouteName);
            duration = itemView.findViewById( R.id.optionsDuration);
            linearLayout = itemView.findViewById( R.id.parentLayout);
            icon = itemView.findViewById( R.id.optionsImageView);

        }
    }

}
