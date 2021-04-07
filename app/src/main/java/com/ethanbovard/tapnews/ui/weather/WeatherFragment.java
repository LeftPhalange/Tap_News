package com.ethanbovard.tapnews.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanbovard.tapnews.R;

public class WeatherFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        weatherViewModel =
                new ViewModelProvider(this).get(WeatherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        final TextView textView = root.findViewById(R.id.cityLabel);
        final TextView tempTextView = root.findViewById(R.id.conditionTextView);
        final RecyclerView daypartView = root.findViewById(R.id.daypartView);
        weatherViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                 textView.setText(s);
            }
        });
        // for temperature and icon
        weatherViewModel.getTextTemp().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tempTextView.setText(s);
            }
        });
        // for narratives
        daypartView.setAdapter(new ForecastAdapter(weatherViewModel.getNarratives().getValue()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        daypartView.setLayoutManager(layoutManager);
        return root;
    }
}