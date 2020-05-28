package com.example.comingsoon;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.opencensus.trace.export.RunningSpanStore;

/**
 * This class is an adapter class to show routes
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> implements Filterable {

    // Properties
    private static final String TAG = "SearchFilterAdapter";

    private ArrayList<String> routes;
    private ArrayList<String> routesFull;
    private Context context;
    private int busIcon;

    // Methods
    public FilterAdapter( Context context, ArrayList<String> routes, int busIcon, SearchTab searchTab) {
        this.context = context;
        this.routes = routes;
        this.busIcon = busIcon;
        routesFull = new ArrayList<>( routes);
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.search_row, parent, false);
        RecyclerView.ViewHolder holder= new FilterAdapter.ViewHolder( view);
        return (FilterAdapter.ViewHolder) holder;
    }

    @Override
    /**
     * This method shows the routes
     */
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, final int position) {
        Log.d( TAG, "onBindViewHolder: called");
        holder.route.setText( routes.get( position));
        holder.icon.setImageResource( busIcon);

        holder.routeLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                if ( context instanceof SearchTab) {

                    Intent intent = new Intent( context, MapActivity.class);
                    int value = ((SearchTab) context).findRouteId( routes.get( position));
                    Log.d( "INTENT", "route id: " + value);
                    intent.putExtra( "route", value);
                    ((SearchTab)context).startActivityForResult( intent, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    @Override
    public Filter getFilter() {
        return routesFilter;
    }

    private Filter routesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filteredRoutes = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredRoutes.addAll( routesFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for ( String string : routesFull) {
                    if ( string.toLowerCase().contains( filterPattern)) {
                        filteredRoutes.add( string);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredRoutes;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            routes.clear();
            routes.addAll( (ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    /**
     * This class is to bind layout.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView route;
        LinearLayout routeLayout;
        ImageView icon;

        public ViewHolder( @NonNull View itemView) {
            super( itemView);
            route = itemView.findViewById( R.id.route_name);
            routeLayout = itemView.findViewById( R.id.route_layout);
            icon = itemView.findViewById( R.id.bus_icon);
        }
    }
}