package com.ethanbovard.tapnews.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ethanbovard.tapnews.MainActivity;
import com.ethanbovard.tapnews.weather.WeatherConditions;
import com.ethanbovard.tapnews.weather.WeatherDataManager;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> weatherLabelText;

    public HomeViewModel() {
        weatherLabelText = new MutableLiveData<>();
    }

    public void updateViewModel (MainActivity mainActivity) {
        WeatherDataManager manager = mainActivity.getWeatherDataManager();
        if (manager != null) {
            String locality = manager.displayLocation.displayName;
            WeatherConditions conditions = manager.displayLocation.getWxConditions();
            String weatherCondition = conditions.conditionText;
            Integer temp = conditions.temperature;
            weatherLabelText.setValue(String.format("%d° and %s in %s. ›", temp, weatherCondition, locality));
        }
    }

    public LiveData<String> getWeatherLabelText() {
        return weatherLabelText;
    }
}