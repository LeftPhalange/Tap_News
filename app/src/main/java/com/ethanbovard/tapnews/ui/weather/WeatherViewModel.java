package com.ethanbovard.tapnews.ui.weather;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ethanbovard.tapnews.models.weatherApi.DailyForecastModel;
import com.ethanbovard.tapnews.weather.WeatherDataManager;
import com.ethanbovard.tapnews.weather.WeatherForecastDay;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<String> cityText;
    private MutableLiveData<String> conditionText;
    private MutableLiveData<Narrative[]> narrativeData;
    private FusedLocationProviderClient fusedLocationClient;
    String tag = "WeatherViewModel";

    public WeatherViewModel(FusedLocationProviderClient locationProviderClient) {

    }

    public void updateViewModel (double[] coords) {
        try {

            WeatherDataManager weatherDataManager = new WeatherDataManager(new double[]{33.74, -84.39});
            cityText = new MutableLiveData<>();
            cityText.setValue(weatherDataManager.displayLocation.displayName);
            conditionText = new MutableLiveData<>();
            conditionText.setValue(weatherDataManager.displayLocation.getWxConditions().temperature.toString() + '°');
            narrativeData = new MutableLiveData<>();
            WeatherForecastDay[] days = weatherDataManager.getForecastDays();
            Narrative[] narratives = new Narrative[7];
            Log.v(tag, "Narratives captured: " + narratives.length);
            for (int i = 0; i < 7; i++) {
                narratives[i] = new Narrative();
                narratives[i].daypartName = days[i].daypartName;
                narratives[i].narrative = days[i].narrative;
                Log.v(tag, "Adding narrative for " + narratives[i].daypartName);
            }
            narrativeData.setValue(narratives);
        }
        catch (Exception exc) {
            Log.e(tag, "Exception caught while gathering weather information");
        }
    }

    public LiveData<String> getText() {
        return cityText;
    }
    public LiveData<String> getTextTemp() {
        return conditionText;
    }
    public LiveData<Narrative[]> getNarratives() {
        return narrativeData;
    }
}