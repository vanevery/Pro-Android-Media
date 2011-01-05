package com.apress.proandroidmedia.ch12.locationtracking;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LocationTracking extends Activity implements LocationListener {

    LocationManager lm;
    TextView tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		tv = (TextView) this.findViewById(R.id.location);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000l, 5.0f, this);
    }

    public void onPause()
    {
        super.onPause();
        lm.removeUpdates(this);
    }

    public void onLocationChanged(Location location) {
        tv.setText(location.getLatitude() + " " + location.getLongitude());
        Log.v("LOCATION", "onLocationChanged: lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
    }

    public void onProviderDisabled(String provider) {
        Log.v("LOCATION", "onProviderDisabled: " + provider);
    }

    public void onProviderEnabled(String provider) {
        Log.v("LOCATION", "onProviderEnabled: " + provider);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v("LOCATION", "onStatusChanged: " + provider + " status:" + status);
        if (status == LocationProvider.AVAILABLE) {
            Log.v("LOCATION","Provider Available");
        } else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
            Log.v("LOCATION","Provider Temporarily Unavailable");
        } else if (status == LocationProvider.OUT_OF_SERVICE) {
            Log.v("LOCATION","Provider Out of Service");
        }
    }
}