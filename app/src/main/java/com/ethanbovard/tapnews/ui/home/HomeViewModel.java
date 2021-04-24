package com.ethanbovard.tapnews.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ethanbovard.tapnews.MainActivity;
import com.ethanbovard.tapnews.weather.WeatherConditions;
import com.ethanbovard.tapnews.weather.WeatherDataManager;

public class HomeViewModel extends ViewModel {

    public MutableLiveData<String> weatherLabelText;

    public HomeViewModel() {
        weatherLabelText = new MutableLiveData<String>();
    }

    public void setWeatherLabelText (String locality, String conditions, Integer temp) {
        if (weatherLabelText == null) {
            weatherLabelText = new MutableLiveData<>();
        }
        Log.v("HomeViewModel", "Weather label changed.");
        weatherLabelText.setValue (String.format("%d° and %s in %s. ›", temp, conditions, locality));
    }

    public LiveData<String> getWeatherLabelText() {
        return weatherLabelText;
    }
}