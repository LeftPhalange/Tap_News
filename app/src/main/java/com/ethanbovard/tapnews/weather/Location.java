package com.ethanbovard.tapnews.weather;

public class Location {
    public String displayName; // i.e. Atlanta
    private WeatherConditions conditions;
    private WeatherForecastDay[] forecastDayparts;
    public Location (String displayName, WeatherConditions conditions, WeatherForecastDay[] forecastDayparts) {
        this.displayName = displayName;
        this.conditions = conditions;
        this.forecastDayparts = forecastDayparts;
    }
    public WeatherForecastDay[] getForecastDayparts () {
        return forecastDayparts;
    }
    public WeatherConditions getWxConditions () {
        return conditions;
    }
}