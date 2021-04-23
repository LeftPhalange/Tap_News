package com.ethanbovard.tapnews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationManager {
    private FusedLocationProviderClient fusedLocationClient;
    private Location location;
    private Context context;
    final int REQUEST_LOCATION = 2;
    public LocationManager(Context context) {
        this.context = context;
        // Get location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // permission has been granted, continue as usual
            Task<Location> locationResult = LocationServices
                    .getFusedLocationProviderClient((Activity)context)
                    .getLastLocation();
        }
    }
    @SuppressLint("MissingPermission")
    public Task<Location> getLocationResult () {
        return fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            updateLocation(location);
                        }
                        else {
                            location = null;
                        }
                    }
                });
    }
    public void updateLocation (Location location) {
        this.location = location;
    }
    public Location getLocation () {
        return location;
    }
    public String getLocality() {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String locationName = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationName = addresses.get(0).getLocality();
        }
        catch (IOException exc) {
            Log.e("LocationManager", "IOException caught while trying to get location");
        }
        return locationName;
    }
}
