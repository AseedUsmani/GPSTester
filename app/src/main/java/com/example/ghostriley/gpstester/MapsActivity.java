package com.example.ghostriley.gpstester;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double lati, longi;
    String latt, longg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Toast.makeText(MapsActivity.this, "Location values cannot be retrieved, exiting now", Toast.LENGTH_LONG).show();
                finish();
            } else {
                lati = Double.parseDouble(extras.getString("Latitude"));
                longi = Double.parseDouble(extras.getString("Longitude"));
            }
        } else {
            String latt = (String) savedInstanceState.getSerializable("Latitude");
            String longg = (String) savedInstanceState.getSerializable("Longitude");
            lati = Double.parseDouble(latt);
            longi = Double.parseDouble(longg);
        } //information retrieved*/

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
        final SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // Add a marker in detected location and move the camera
        LatLng latLng;
        for (int i = 1; i < 6; i++) {
            latt = sharedPreferences.getString(Integer.toString(i) + "a", "");
            longg = sharedPreferences.getString(Integer.toString(i) + "o", "");

            if (latt != "" && longg != "") {
                lati = Double.parseDouble(latt);
                longi = Double.parseDouble(longg);
                latLng = new LatLng(lati, longi); // lati, longi are latitude and longitude of last detected location
                if (i == 5) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Last location"));
                } else {
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Detected location"));
                }


                latLng = new LatLng(lati, longi);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to location user
                        .zoom(15)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.showInfoWindow();
                        return false;
                    }
                });
            }
        }

        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Toast.makeText(MapsActivity.this, "Clicked on marker", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

    }
}
