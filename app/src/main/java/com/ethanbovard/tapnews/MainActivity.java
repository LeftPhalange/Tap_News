package com.ethanbovard.tapnews;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ethanbovard.tapnews.weather.WeatherDataManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    WeatherDataManager weatherDataManager;
    private com.ethanbovard.tapnews.LocationManager locationManager;
    public Location currentLocation;
    public LocationManager lManager;
    private final int AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create location manager
        lManager = new com.ethanbovard.tapnews.LocationManager(this);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString (R.string.geocode_weather);
        String geocode = sharedPref.getString(getString(R.string.geocode_weather), defaultValue);
        // Custom location? No problem
        if (!geocode.equals("PHYSICAL_LOCATION")) {
            // Set up weather data for the fragment and home screen to use
            double latitude = parseDouble(geocode.split(",")[0]);
            double longitude = parseDouble(geocode.split(",")[1]);
            createWeatherDataManager(latitude, longitude);
        }
        else {
            lManager.getLocationResult(this).addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        createWeatherDataManager(location);
                        setCurrentLocation(location);
                        TextView navToWeatherButton = findViewById(R.id.navToWeatherButton);
                        navToWeatherButton.setText(Util.returnWeatherButtonText(weatherDataManager));
                    }
                    else {
                        createWeatherDataManager(33.74, -84.39); // provide default location
                        TextView navToWeatherButton = findViewById(R.id.navToWeatherButton);
                        navToWeatherButton.setText(Util.returnWeatherButtonText(weatherDataManager));
                    }
                }
            });
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
    }

    public WeatherDataManager getWeatherDataManager () {
        return weatherDataManager;
    }

    public void setCurrentLocation (Location location) {
        this.currentLocation = location;
    }

    public MainActivity getThis () {
        return this;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void replaceWeatherDataManager(WeatherDataManager weatherDataManager) {
        this.weatherDataManager = weatherDataManager;
    }
    public com.ethanbovard.tapnews.LocationManager getLocationManager() { return locationManager; }
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
        // needs LocationManager
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