package com.example.ghostriley.gpstester;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, android.content.DialogInterface.OnClickListener {

    private EditText editTextShowLocation;
    private Button buttonGetLocation, buttonShowOnMap, buttonClearMemory;
    private ProgressBar progress;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();

    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    public String lati;
    public String longi;
    public static int count = 0;
    int flag = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        final SharedPreferences sharedPreferences2 = getSharedPreferences("Data", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences2.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextShowLocation = (EditText) findViewById(R.id.locationText);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);

        buttonShowOnMap = (Button) findViewById(R.id.showOnMapButton);
        buttonGetLocation = (Button) findViewById(R.id.getLocationButton);
        buttonClearMemory = (Button) findViewById(R.id.clearButton);

        buttonGetLocation.setOnClickListener(this);

        buttonShowOnMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapIntent);
            }
        });

        buttonClearMemory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "Data cleared", Toast.LENGTH_LONG).show();
                count = 1;
            }
        });


        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View v) {
        progress.setVisibility(View.VISIBLE);
        // exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        // don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder builder = new Builder(this);
            builder.setTitle("Attention!");
            builder.setMessage("Sorry, location is not determined. Please enable location providers");
            builder.setPositiveButton("OK", this);
            builder.setNeutralButton("Cancel", this);
            builder.create().show();
            progress.setVisibility(View.GONE);
        }

        if (gps_enabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        }
        if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
        }
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // This needs to stop getting the location data and save the battery power.
                locManager.removeUpdates(locListener);

                lati = Double.toString(location.getLatitude());
                longi = Double.toString(location.getLongitude());
                String londitude = "Londitude: " + location.getLongitude();
                String latitude = "Latitude: " + location.getLatitude();
                String altitiude = "Altitiude: " + location.getAltitude();
                String accuracy = "Accuracy: " + location.getAccuracy();
                String time = "Time: " + location.getTime();

                editTextShowLocation.setText(londitude + "\n" + latitude + "\n" + altitiude + "\n" + accuracy + "\n" + time);
                flag = 1;
                progress.setVisibility(View.GONE);
                final SharedPreferences sharedPreferences2 = getSharedPreferences("Data", MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences2.edit();
                for (int i = 1; i < 5; i++) {
                    // a: LaTITUDE; o: LoNGITUDE
                    editor.putString(Integer.toString(i)+"a", sharedPreferences2.getString(Integer.toString(i + 1)+"a", ""));
                    editor.putString(Integer.toString(i)+"o", sharedPreferences2.getString(Integer.toString(i + 1)+"o", ""));
                }
                editor.putString("5" + "a", lati);
                editor.putString("5" + "o", longi);
                editor.commit();
            }
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
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEUTRAL) {
            editTextShowLocation.setText("Sorry, location is not determined. To fix this please enable location providers");
        } else if (which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
}