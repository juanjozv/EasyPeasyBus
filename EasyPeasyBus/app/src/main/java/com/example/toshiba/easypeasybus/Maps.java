package com.example.toshiba.easypeasybus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Collection.*;
import java.util.Collections;
import  java.util.stream.Stream;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

public class Maps extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, DirectionCallback {
    private CardView btnRequestDirection;
    private GoogleMap mMap;
    private String serverKey = "AIzaSyCoym0yWt2nGhROurj_RESVXZCryKGxaws";
    private LatLng origin = new LatLng(10.0159631, -84.21416699999999);
    private LatLng destination = new LatLng(9.9277704, -84.0908422);
    private List<Polyline> polylines = new ArrayList<Polyline>();
    private List<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnRequestDirection = findViewById(R.id.btn_request_direction);
        btnRequestDirection.setOnClickListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setOnPlaceSelectedListener(R.id.place_autocomplete_fragment_origin);
        setOnPlaceSelectedListener(R.id.place_autocomplete_fragment_destiny);

    }
    
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_request_direction) {
            requestDirection();
        }
    }

    public void requestDirection() {
        Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        refreshMap();
        Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            Leg leg = route.getLegList().get(0);
            ArrayList<LatLng> sectionPositionList = leg.getSectionPoint();
            /*for (LatLng position : sectionPositionList) {
                mMap.addMarker(new MarkerOptions().position(position));
            }*/
            markers.add(mMap.addMarker(new MarkerOptions().position(sectionPositionList.get(0))));
            int size = sectionPositionList.size() - 1;
           markers.add(mMap.addMarker(new MarkerOptions().position(sectionPositionList.get(size))));

            List<Step> stepList = leg.getStepList();
            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
            for (PolylineOptions polylineOption : polylineOptionList) {
                polylines.add(mMap.addPolyline(polylineOption));
            }
            setCameraWithCoordinationBounds(route);

            //btnRequestDirection.setVisibility(View.GONE);
        } else {
            Snackbar.make(btnRequestDirection, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    public PlaceSelectionListener obtainPlaceSelectionListener(String location) {
        PlaceSelectionListener listener;
        if(location == "Origen") {
            listener = new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    origin = place.getLatLng();
                }
                @Override
                public void onError(Status status) {}
            };
        }else {
            listener = new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    destination = place.getLatLng();
                }
                @Override
                public void onError(Status status) {}
            };

        }
        return listener;
    }

    public void setOnPlaceSelectedListener(final int ref){
        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(ref);
        placeAutocompleteFragment.setHint(ref == R.id.place_autocomplete_fragment_origin ? "Ingresar origen" : "Ingresar destino");
        String listenerType = ref == (R.id.place_autocomplete_fragment_origin) ? "Origen" : "Destino";
        PlaceSelectionListener listener = obtainPlaceSelectionListener(listenerType);
        placeAutocompleteFragment.setOnPlaceSelectedListener(listener);
    }

    public void refreshMap(){
        for(Marker marker : markers)
            marker.remove();
        markers.clear();

        for(Polyline line : polylines)
            line.remove();
        polylines.clear();
    }

    
    
}
