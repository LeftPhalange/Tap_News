package com.ethanbovard.tapnews.weather;

import android.content.Context;
import android.util.Log;

import com.ethanbovard.tapnews.JSONManager;
import com.ethanbovard.tapnews.Util;
import com.ethanbovard.tapnews.models.weatherApi.DailyForecastModel;
import com.ethanbovard.tapnews.models.weatherApi.NowcastModel;
import com.ethanbovard.tapnews.models.weatherApi.ObservationsModel;

import java.net.URL;
import java.util.Arrays;

public class WeatherDataManager {
    public Location displayLocation;
    private static final String TAG = "WeatherDataManager";
    double[] coordinates = new double[] { 40.7128, -74.0060 }; // default is New York City
    // i.e. new Integer[] { 33.74, -84.39 } for coordinates
    private final String API_KEY = ""; // TODO: Add API key for api.weather.com
    private final String baseObsUrl = "https://api.weather.com/v3/wx/observations/current?geocode=%s&format=json&units=e&language=en-US&apiKey=%s";
    private final String baseDailyUrl = "https://api.weather.com/v3/wx/forecast/daily/7day?geocode=%s&format=json&units=e&language=en-US&apiKey=%s";
    private final String baseNowcastUrl = "https://api.weather.com/v1/geocode/%f/%f/forecast/nowcast.json?apiKey=%s&language=en-US&units=e"; // change API key here before compiling
    public WeatherDataManager(double[] coordinates, Context context) throws Exception {
        Log.v(TAG, "started WeatherDataManager");
        this.coordinates = coordinates;
        Log.v(TAG, String.format("Getting weather information from coordinates %s...", Arrays.toString(coordinates)));
        WeatherConditions conditions = populateWeatherConditions();
        String nowcast = populateNowcast();
        displayLocation = new Location(Util.getLocalityFromCoords(coordinates[0], coordinates[1], context), conditions, null, nowcast);
    }
    public String populateNowcast () {
        NowcastModel nowcastModel = null;
        try {
            nowcastModel = (NowcastModel) JSONManager.Deserialize(new URL(String.format(baseNowcastUrl, coordinates[0], coordinates[1])), NowcastModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowcastModel.forecast.narrative_32char;
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
