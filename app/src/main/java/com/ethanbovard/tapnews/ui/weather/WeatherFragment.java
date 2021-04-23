package com.ethanbovard.tapnews.ui.weather;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanbovard.tapnews.R;
import com.ethanbovard.tapnews.Util;

public class WeatherFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    private Context context;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        weatherViewModel =
                new ViewModelProvider(this).get(WeatherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        final TextView textView = root.findViewById(R.id.cityLabel);
        final TextView tempTextView = root.findViewById(R.id.conditionTextView);
        final TextView expectTempLabel = root.findViewById(R.id.expectTempLabel);
        final RecyclerView daypartView = root.findViewById(R.id.daypartView);
        final ImageView conditionIconView = root.findViewById(R.id.conditionIconView);
        weatherViewModel.updateViewModel(root.getContext());
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
        // for "expect this temp" messages
        weatherViewModel.getExpectMsg().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                expectTempLabel.setText(s);
            }
        });
        // for drawable representing the icon
        weatherViewModel.getIconFileName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Context context = getActivity();
                Resources resources = context.getResources();
                final int resourceId = resources.getIdentifier(s, "drawable",
                        context.getPackageName());
                Log.v("WeatherViewModel", String.format("Setting image resource to resource ID %d...", resourceId));
                conditionIconView.setImageResource(resourceId);
            }
        });
        weatherViewModel.getNarratives().observe(getViewLifecycleOwner(), new Observer<Narrative[]>() {
           @Override
           public void onChanged (Narrative[] narratives) {
               // for narratives
               daypartView.setAdapter(new ForecastAdapter(narratives));
               LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
               daypartView.setLayoutManager(layoutManager);
           }
        });
        return root;
    }
}