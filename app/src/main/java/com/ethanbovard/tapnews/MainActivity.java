package com.ethanbovard.tapnews;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import com.ethanbovard.tapnews.weather.WeatherDataManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    WeatherDataManager weatherDataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up shared preferences
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.geocode_weather), Context.MODE_PRIVATE);
        String geocode = sharedPref.getString(String.valueOf(R.string.geocode_weather), "33.74,-84.39");
        if (geocode == "PHYSICAL_LOCATION") {
            com.ethanbovard.tapnews.LocationManager locationManager = new com.ethanbovard.tapnews.LocationManager(this);
            locationManager.getLocationResult().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        createWeatherDataManager(location);
                    }
                    else {
                        // In those rare times when you can't get physical location... provide a default :-/
                        double[] nycDefaultCoords = new double[] { 40.7128, -74.0060 };
                        createWeatherDataManager(nycDefaultCoords[0], nycDefaultCoords[1]);
                    }
                }
            });
        }
        else {
            // Set up weather data for the fragment and home screen to use
            double latitude = parseDouble(geocode.split(",")[0]);
            double longitude = parseDouble(geocode.split(",")[1]);
            try {
                weatherDataManager = new WeatherDataManager(new double[]{latitude, longitude}, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_weather, R.id.navigation_traffic, R.id.navigation_settings)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        // Set up location stuff
    }
    public WeatherDataManager getWeatherDataManager () {
        return weatherDataManager;
    }
    public void createWeatherDataManager (Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        try {
            weatherDataManager = new WeatherDataManager(new double[] { latitude, longitude }, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createWeatherDataManager (double latitude, double longitude) {
        try {
            weatherDataManager = new WeatherDataManager(new double[] { latitude, longitude }, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void changeToFragment (int id) {
        // must be from R.id.navigation_
        if (navController != null) {
            navController.navigate(id);
        }
    }
}