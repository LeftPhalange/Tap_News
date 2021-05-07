package com.ethanbovard.tapnews.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanbovard.tapnews.MainActivity;
import com.ethanbovard.tapnews.R;
import com.ethanbovard.tapnews.Util;
import com.ethanbovard.tapnews.weather.WeatherDataManager;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView articlesView;
    private TextView dateTextView;
    private TextView navToWeatherButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        navToWeatherButton = root.findViewById(R.id.navToWeatherButton);
        Observer<String> navButtonObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                navToWeatherButton.setText(s);
            }
        };
        MainActivity mainActivityContext = (MainActivity)getActivity();
        navToWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityContext.changeToFragment(R.id.navigation_weather);
            }
        });
        homeViewModel.getWeatherLabelText().observe(getViewLifecycleOwner(), navButtonObserver);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Set up the weather link
        WeatherDataManager weatherDataManager = ((MainActivity)view.getContext()).getWeatherDataManager();
        if (weatherDataManager != null) {
            Log.v("HomeViewModel", "Got a valid WeatherDataManager");
            homeViewModel.setWeatherLabelText(weatherDataManager.displayLocation.displayName, weatherDataManager.displayLocation.getWxConditions().conditionText,
                    weatherDataManager.displayLocation.getWxConditions().temperature);
            ((TextView) getView().findViewById(R.id.navToWeatherButton)).setText(homeViewModel.getWeatherLabelText().getValue());
        }
        // Set the date
        dateTextView = getView().findViewById(R.id.dateTextView);
        dateTextView.setText(Util.getDateString());
        // For news articles
        articlesView = getView().findViewById(R.id.articlesView);
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.makeAPIRequest(articlesView, getContext());
    }
}