package com.ethanbovard.tapnews;

import android.graphics.drawable.Drawable;

import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    public static String returnTempDay (int highTemp, int lowTemp) {
        boolean isFirstDayNull = (highTemp == Integer.MIN_VALUE);
        return isFirstDayNull ? String.format("Expect a low of %d° tonight.", lowTemp) : String.format("Expect a high of %d° today.", highTemp);
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