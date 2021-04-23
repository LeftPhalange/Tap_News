package com.ethanbovard.tapnews.weather;

import android.util.Log;

import com.ethanbovard.tapnews.JSONManager;
import com.ethanbovard.tapnews.models.weatherApi.DailyForecastModel;
import com.ethanbovard.tapnews.models.weatherApi.DaypartModel;
import com.ethanbovard.tapnews.models.weatherApi.ObservationsModel;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherDataManager {
    public Location displayLocation;
    private static final String TAG = "dataManager";
    double[] coordinates = new double[] { 40.7128, -74.0060 }; // default is New York City
    // i.e. new Integer[] { 33.74, -84.39 } for coordinates
    private String API_KEY = "6532d6454b8aa370768e63d6ba5a832e";
    private String baseObsUrl = "https://api.weather.com/v3/wx/observations/current?geocode=%s&format=json&units=e&language=en-US&apiKey=%s";
    private String baseDailyUrl = "https://api.weather.com/v3/wx/forecast/daily/7day?geocode=%s&format=json&units=e&language=en-US&apiKey=%s";
    public WeatherDataManager(double[] coordinates) throws Exception {
        Log.v(TAG, "started WeatherDataManager");
        WeatherConditions conditions = populateWeatherConditions();
        displayLocation = new Location("--", conditions, null);
    }
    public WeatherConditions populateWeatherConditions () throws Exception {
        WeatherConditions conditions = new WeatherConditions();
        String finalCoords = String.format("%f,%f", coordinates[0], coordinates[1]);
        ObservationsModel model = (ObservationsModel)JSONManager.Deserialize(new URL(String.format(baseObsUrl, finalCoords, API_KEY)), ObservationsModel.class);
        conditions.conditionText = model.wxPhraseShort;
        conditions.temperature = model.temperature;
        conditions.conditionIcon = model.iconCode;
        return conditions;
    }
    public WeatherForecastDay[] getForecastDays () throws Exception {
        String finalCoords = String.format("%f,%f", coordinates[0], coordinates[1]);
        DailyForecastModel dailyForecastModel = (DailyForecastModel) JSONManager.Deserialize(new URL(String.format(baseDailyUrl, finalCoords, API_KEY)), DailyForecastModel.class);
        WeatherForecastDay[] forecastDays = new WeatherForecastDay[dailyForecastModel.narrative.length];
        for (int i = 0; i < forecastDays.length; i++) {
            forecastDays[i] = new WeatherForecastDay();
            forecastDays[i].narrative = dailyForecastModel.narrative[i];
            forecastDays[i].daypartName = dailyForecastModel.dayOfWeek[i];
            forecastDays[i].lowTemp = dailyForecastModel.temperatureMin[i];
            forecastDays[i].highTemp = dailyForecastModel.temperatureMax[i] != null ? dailyForecastModel.temperatureMax[i] : Integer.MIN_VALUE;
        }
        return forecastDays;
    }
}