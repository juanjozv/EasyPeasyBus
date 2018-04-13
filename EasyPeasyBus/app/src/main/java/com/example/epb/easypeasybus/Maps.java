package com.example.epb.easypeasybus;

        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.UiSettings;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.PolylineOptions;
        import com.google.maps.DirectionsApi;
        import com.google.maps.GeoApiContext;
        import com.google.maps.android.PolyUtil;
        import com.google.maps.errors.ApiException;
        import com.google.maps.model.DirectionsResult;
        import com.google.maps.model.TravelMode;

        import org.joda.time.DateTime;

        import java.io.IOException;
        import java.util.List;
        import java.util.concurrent.TimeUnit;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public Maps() throws InterruptedException, ApiException, IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




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
        mMap = googleMap;

        /*// Add a marker in Sydney and move the camera
        LatLng actualLocation = new LatLng(9.988597,-84.097419);
        mMap.addMarker(new MarkerOptions()
                .position(actualLocation)
                .title("Marker in your actual position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(actualLocation));
        mMap.getUiSettings().setZoomControlsEnabled(true);*/



        addMarkersToMap(result, mMap);
        addPolyline(result, mMap);

    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    DateTime now = new DateTime();
    LatLng actualLocation = new LatLng(9.988597,-84.097419);
    LatLng destinyLocation = new LatLng(9.984507,-84.086754);
    DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
            .mode(TravelMode.DRIVING).origin(actualLocation.toString()).destination(destinyLocation.toString()).departureTime(now).await();



    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].startLocation.lat,results.routes[0].legs[0].startLocation.lng)).title(results.routes[0].legs[0].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].endLocation.lat,results.routes[0].legs[0].endLocation.lng)).title(results.routes[0].legs[0].startAddress).snippet(getEndLocationTitle(results)));
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  "Time :"+ results.routes[0].legs[0].duration.humanReadable + " Distance :" + results.routes[0].legs[0].distance.humanReadable;
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }
}
