package com.ethanbovard.tapnews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationManager {
    private final FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private final Context context;
    final int REQUEST_LOCATION = 2;

    public LocationManager(Context context) {
        this.context = context;
        Log.i("LocationManager", "started");
        // Get location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // permission has been granted, continue as usual
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                    }
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    public Task<Location> getLocationResult(Context context) {
        return fusedLocationClient.getLastLocation();
    }

    public void updateLocation(Location location) {
        this.currentLocation = location;
    }

    public Location getLocation() {
        return currentLocation;
    }
}
