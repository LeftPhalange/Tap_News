package com.ethanbovard.tapnews.ui.weather;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ethanbovard.tapnews.MainActivity;
import com.ethanbovard.tapnews.Util;
import com.ethanbovard.tapnews.weather.WeatherConditions;
import com.ethanbovard.tapnews.weather.WeatherDataManager;
import com.ethanbovard.tapnews.weather.WeatherForecastDay;

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<String> cityText;
    private MutableLiveData<String> conditionText;
    private MutableLiveData<Narrative[]> narrativeData;
    private MutableLiveData<String> expectMsg;
    private MutableLiveData<String> iconFileName;
    private MutableLiveData<String> nowcastText;
    private Context context;
    String tag = "WeatherViewModel";

    public WeatherViewModel() {

    }

    public void updateViewModel(Context context) {
        // set up LocationManager
        this.context = context;
        try {
            // Prepare view model fields
            expectMsg = new MutableLiveData<>();
            cityText = new MutableLiveData<>();
            conditionText = new MutableLiveData<>();
            narrativeData = new MutableLiveData<>();
            iconFileName = new MutableLiveData<>();
            nowcastText = new MutableLiveData<>();
            Log.v(tag, "Is context null: " + context);
            process ();
        }
        catch (Exception exc) {
            Log.e(tag, "Exception caught while gathering weather information");
        }
    }

    public void process () {
        WeatherDataManager weatherDataManager = null;
        WeatherForecastDay[] days = null;
        try {
            weatherDataManager = ((MainActivity)context).getWeatherDataManager();
            days = weatherDataManager.getForecastDays();
        }
        catch (Exception exc) {
            Log.e(tag, "Exception caught when getting location or weather information.");
        }
        finally {
            Narrative[] narratives = new Narrative[7];
            Log.v(tag, "Narratives captured: " + narratives.length);
            for (int i = 0; i < 7; i++) {
                narratives[i] = new Narrative();
                narratives[i].daypartName = days[i].daypartName;
                narratives[i].narrative = days[i].narrative;
                Log.v(tag, "Adding narrative for " + narratives[i].daypartName);
            }
            expectMsg.setValue(Util.returnTempDay(days[0].highTemp, days[0].lowTemp));
            WeatherConditions conditions = weatherDataManager.displayLocation.getWxConditions();
            cityText.setValue(weatherDataManager.displayLocation.displayName);
            conditionText.setValue(conditions.temperature.toString() + '°');
            narrativeData.setValue(narratives);
            nowcastText.setValue(weatherDataManager.displayLocation.nowcast);
            iconFileName.setValue(Util.returnFileName(conditions.conditionIcon));
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
    public LiveData<String> getExpectMsg() {
        return expectMsg;
    }
    public LiveData<String> getIconFileName() {
        return iconFileName;
    }
    public LiveData<String> getNowcast() {
        return nowcastText;
    }
}