package com.ethanbovard.tapnews;

import java.util.Calendar;
import java.util.Date;

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
}