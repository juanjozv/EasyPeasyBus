package com.example.toshiba.easypeasybus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class Maps extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, DirectionCallback {
    private static final int REQUEST_LOCATION = 1;

    private CardView btnRequestDirection;
    private GoogleMap mMap;
    private String serverKey = "AIzaSyCoym0yWt2nGhROurj_RESVXZCryKGxaws";
    private LatLng origin;
    private LatLng destination;
    private List<Polyline> polylines = new ArrayList<Polyline>();
    private List<Marker> markers = new ArrayList<Marker>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
        LatLng costaRica = new LatLng(9.923776, -84.088798);
        int zoomLevel = 14;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(costaRica, zoomLevel));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_request_direction) {
            requestDirection();
        }
    }



    public void requestDirection() {
        Snackbar.make(btnRequestDirection, "Generando ruta...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

    public void showDurationDistance(String duration, String distance) {
        TextView dura = (TextView) findViewById(R.id.duracion);
        dura.setText(duration);
        TextView dis = (TextView) findViewById(R.id.distancia);
        dis.setText(distance);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        refreshMap();
        Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            Leg leg = route.getLegList().get(0);

            Info distanceInfo = leg.getDistance();
            Info durationInfo = leg.getDuration();
            String distance = distanceInfo.getText();
            String duration = durationInfo.getText();

            showDurationDistance(duration, distance);

            ArrayList<LatLng> sectionPositionList = leg.getSectionPoint();
           /* for (LatLng position : sectionPositionList) {
                mMap.addMarker(new MarkerOptions().position(position)
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_directions_bus_black))
                        .title("Parada de autobús")
                );
            }*/
            markers.add(mMap.addMarker(new MarkerOptions().position(sectionPositionList.get(0))));
            int size = sectionPositionList.size() - 1;

           markers.add(
                   mMap.addMarker(new MarkerOptions().position(sectionPositionList.get(size))
                           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
           ));

            List<Step> stepList = leg.getStepList();
            int count = 0;
            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
            for (PolylineOptions polylineOption : polylineOptionList) {
                polylines.add(mMap.addPolyline(
                        polylineOption.color(Color.parseColor("#3399FF"))

                ));


                    List<LatLng> coordinates = polylineOption.getPoints();
                    for(LatLng coordinate : coordinates){
                        if(count++ % 24 == 0) {
                            markers.add(mMap.addMarker(new MarkerOptions().position(coordinate)
                                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_directions_bus_black))
                                    .title("Parada de autobús")
                            ));
                        }
                    }


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
        return new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if(location == "Origen")
                    origin = place.getLatLng();
                else
                    destination = place.getLatLng();
            }
            @Override
            public void onError(Status status) {}
        };
    }

    public void setOnPlaceSelectedListener(final int ref){
        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(ref);
        placeAutocompleteFragment.setHint(ref == R.id.place_autocomplete_fragment_origin ? "Ingresar origen" : "Ingresar destino");
        String listenerType = ref == (R.id.place_autocomplete_fragment_origin) ? "Origen" : "Destino";
        PlaceSelectionListener listener = obtainPlaceSelectionListener(listenerType);
        placeAutocompleteFragment.setOnPlaceSelectedListener(listener);

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("CR")
                .build();

        placeAutocompleteFragment.setFilter(autocompleteFilter);
    }

    public void refreshMap(){
        for(Marker marker : markers)
            marker.remove();
        markers.clear();

        for(Polyline line : polylines)
            line.remove();
        polylines.clear();
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
