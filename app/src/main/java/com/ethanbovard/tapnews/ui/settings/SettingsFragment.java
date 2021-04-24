package com.ethanbovard.tapnews.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.ethanbovard.tapnews.MainActivity;
import com.ethanbovard.tapnews.R;
import com.ethanbovard.tapnews.Util;
import com.ethanbovard.tapnews.weather.WeatherDataManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private Switch customLocationSwitch;
    private Switch physicalLocationSwitch;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_fragment, container, false);
        Context context = getContext();
        if (!Places.isInitialized()) {
            Places.initialize(context, getString(R.string.places_api_key));
        }

        // Set up switches
        customLocationSwitch = root.findViewById(R.id.customLocationSwitch);
        physicalLocationSwitch = root.findViewById(R.id.physicalLocationSwitch);

        // Check if physical or custom location, change switch text based on that
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.geocode_weather);
        String geocode = sharedPref.getString(getString(R.string.geocode_weather), defaultValue);
        String localityName = ((MainActivity)context).getWeatherDataManager().displayLocation.displayName;
        customLocationSwitch.setText("Use my custom location\n(current: " + localityName + ')');

        customLocationSwitch.setChecked(geocode.contains(","));
        physicalLocationSwitch.setChecked(geocode.equals("PHYSICAL_LOCATION"));

        // Set listeners for both switches
        customLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (physicalLocationSwitch != null && isChecked) {
                    physicalLocationSwitch.setChecked(false);
                    // Set the fields to specify which types of place data to
                    // return after the user has made a selection.
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(context);
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                }
                else if (!isChecked && physicalLocationSwitch != null) {
                    physicalLocationSwitch.setChecked(true);
                }
            }
        });
        physicalLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (customLocationSwitch != null && isChecked) {
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.geocode_weather), "PHYSICAL_LOCATION");
                    editor.apply();
                    Location location = ((MainActivity)getActivity()).getCurrentLocation();
                    try {
                        ((MainActivity)getActivity()).replaceWeatherDataManager(new WeatherDataManager(new double[] { location.getLatitude(), location.getLongitude() }, getContext()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    customLocationSwitch.toggle();
                    customLocationSwitch.setText(String.format("Use my custom location\n(current: %s)", ((MainActivity)getActivity()).getWeatherDataManager().displayLocation.displayName));
                }

                else if (!isChecked && customLocationSwitch != null) {
                    customLocationSwitch.setChecked(true);
                }
            }
        });
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                customLocationSwitch.setText("Use my custom location\n(current: " + place.getName() + ')');
                // Commit to SharedPreferences
                LatLng latLng = place.getLatLng();
                String geocodeToSave = String.format("%f,%f", latLng.latitude, latLng.longitude);
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.geocode_weather), geocodeToSave);
                editor.apply();
                Toast.makeText(getContext(), "Changes made.", Toast.LENGTH_LONG).show();
                try {
                    ((MainActivity)getActivity()).replaceWeatherDataManager(new WeatherDataManager(new double[] { latLng.latitude, latLng.longitude }, getContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                physicalLocationSwitch.toggle();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }
}