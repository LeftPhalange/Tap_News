package com.ethanbovard.tapnews.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanbovard.tapnews.MainActivity;
import com.ethanbovard.tapnews.R;
import com.ethanbovard.tapnews.Util;
import com.ethanbovard.tapnews.ui.weather.ForecastAdapter;
import com.ethanbovard.tapnews.weather.WeatherDataManager;

import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView articlesView;
    private TextView dateTextView;
    private TextView navToWeatherButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        navToWeatherButton = root.findViewById(R.id.navToWeatherButton);
        MainActivity mainActivityContext = ((MainActivity)root.getContext());
        navToWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityContext.changeToFragment(R.id.navigation_weather);
            }
        });
        homeViewModel.getWeatherLabelText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                navToWeatherButton.setText(s);
            }
        });
        homeViewModel.updateViewModel(mainActivityContext);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Set the date
        dateTextView = (TextView) getView().findViewById(R.id.dateTextView);
        dateTextView.setText(Util.getDateString());
        // For news articles
        articlesView = (RecyclerView) getView().findViewById(R.id.articlesView);
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.makeAPIRequest(articlesView, getContext());
    }
}