package com.example.runningpacenotifier;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.provider.ProviderProperties;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LocationService implements LocationListener {

    public static final long MIN_TIME = 1;
    public static final long MIN_DISTANCE = 10;

    private double pace;

    private LocationManager locationManager;
    private Location lastLocation;
    private List<Location> locations = new ArrayList<>();

    public LocationService(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    /**
     * Convert the pace from m/s to min/hr
     * @param pace the pace to convert
     * @return the new pace
     */
    private double convertPace(double pace) {
        return 26.332 / pace;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locations.add(location);
        pace = convertPace(location.getSpeed());
        System.out.println("Pace: " + pace);
        lastLocation = location;

    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void stop() {
        locationManager.removeUpdates(this);
    }
}