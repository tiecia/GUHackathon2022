package com.example.runningpacenotifier;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.provider.ProviderProperties;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.InputStream;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class LocationService implements LocationListener {

    public static final long MIN_TIME = 1;
    public static final long MIN_DISTANCE = 10;

    private double pace;

    private LocationManager locationManager;
    private Location lastLocation;
    private List<Location> locations = new ArrayList<>();

    private Timer locationTimer;
    private TimerTask locationTimerTask;

    private Context context;
    private Activity activity;

    private FusedLocationProviderClient fusedLocationProviderClient;

    protected LocationRequest locationRequest;

    @SuppressLint("MissingPermission")
    public LocationService(Context context, Activity activity, FusedLocationProviderClient fusedLocationProviderClient) {
        this.context = context;
        this.activity = activity;
        this.fusedLocationProviderClient = fusedLocationProviderClient;

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());



//        LocationRequest request = LocationRequest.create();
//        request.setInterval(1000);
//        request.setFastestInterval(1000);
//        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        PendingIntent locationUpdateIntent = PendingIntent.OnFinished() {
//            @Override
//            public void onSendFinished(PendingIntent pendingIntent, Intent intent, int resultCode, String resultData, Bundle resultExtras) {
//
//            }
//        };
//        fusedLocationProviderClient.requestLocationUpdates(request, );

        requestLocationPermission();

//        requestBackgroundLocationPermission();
        System.out.println("LocationService()");
//        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location != null) {
//                    System.out.println("Speed: " + location.getSpeed());
//                    System.out.println("Location: " + location.getLatitude() + "," + location.getLongitude());
//                } else {
//                    System.out.println("Location null");
//                }
//            }
//        });

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        locationTimerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("LocationTask");
                Consumer<Location> consumer = new Consumer<Location>() {
                    @Override
                    public void accept(Location location) {
                        if(location != null) {
                            System.out.println("Speed: " + location.getSpeed());
                            System.out.println("Position: " + location.getLatitude() + ", " + location.getLongitude());
                        } else {
                            System.out.println("Location null");
                        }
                    }


                };

                getLocation(consumer);
            }
        };

        locationTimer = new Timer(true);
        locationTimer.scheduleAtFixedRate(locationTimerTask, 0, 1000);



//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
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

    @SuppressLint("MissingPermission")
    public void getLocation(Consumer<Location> consumer) {
        requestLocationPermission();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.getCurrentLocation(
                LocationManager.FUSED_PROVIDER,
                null,
                context.getMainExecutor(),
                consumer);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Not enough permissions for location.");
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    private void requestBackgroundLocationPermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Not enough permissions for location.");
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
        }
    }

    public void stop() {
        locationManager.removeUpdates(this);
    }
}