package com.ethanbovard.tapnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Geocoder;
import android.util.Log;

import com.ethanbovard.tapnews.weather.WeatherDataManager;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Util {
    public static String generateGreeting (Calendar time) {
        int hour = time.get(Calendar.HOUR_OF_DAY);
        if (hour >= 5 && hour <= 11) {
            return "Good morning, %s";
        }
        else if (hour >= 12 && hour <= 15) {
            return "Good afternoon, %s";
        }
        else {
            return "Good evening, %s";
        }
    }
    public String returnCompact (String str) {
        return str.replace(" ", "+");
    }
    public static String returnWeatherButtonText (WeatherDataManager weatherDataManager) {
        Integer temp = weatherDataManager.displayLocation.getWxConditions().temperature;
        String conditions = weatherDataManager.displayLocation.getWxConditions().conditionText;
        String locality = weatherDataManager.displayLocation.displayName;
        return String.format("%d° and %s in %s. ›", temp, conditions, locality);
    }
    public static String returnTempDay (int highTemp, int lowTemp) {
        boolean isFirstDayNull = (highTemp == Integer.MIN_VALUE);
        return isFirstDayNull ? String.format("Expect a low of %d° tonight.", lowTemp) : String.format("Expect a high of %d° today.", highTemp);
    }
    @SuppressLint("LongLogTag")
    public static String getLocalityFromCoords (double lat, double lon, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        String locality = "--";
        try {
            locality = geocoder.getFromLocation(lat, lon, 1)
                    .get(0)
                    .getLocality();
        }
        catch (IOException exc) {
            Log.e("Util.getLocalityFromCoords", "Error while trying to get locality (IOException)");
        }
        return locality;
    }
    // iconCodeExtended must be used
    public static String returnFileName (Integer iconCode) {
        return "icon" + iconCode;
    }
    public static String getDateString () {
        Date date = Calendar.getInstance().getTime();
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        return df.format(date).toUpperCase();
    }
    public static String getWeatherIcon (String condition) {
        return "";
    }
}