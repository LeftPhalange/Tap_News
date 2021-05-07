package com.ethanbovard.tapnews.weather;

public class Location {
    public String displayName; // i.e. Atlanta
    public String nowcast;
    private final WeatherConditions conditions;
    private final WeatherForecastDay[] forecastDayparts;
    public Location (String displayName, WeatherConditions conditions, WeatherForecastDay[] forecastDayparts, String nowcast) {
        this.displayName = displayName;
        this.conditions = conditions;
        this.forecastDayparts = forecastDayparts;
        this.nowcast = nowcast;
    }
    public WeatherForecastDay[] getForecastDayparts () {
        return forecastDayparts;
    }
    public WeatherConditions getWxConditions () {
        return conditions;
    }
}
