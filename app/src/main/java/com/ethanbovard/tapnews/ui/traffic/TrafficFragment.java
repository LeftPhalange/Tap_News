package com.ethanbovard.tapnews.ui.traffic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ethanbovard.tapnews.JSONManager;
import com.ethanbovard.tapnews.MainActivity;
import com.ethanbovard.tapnews.R;
import com.ethanbovard.tapnews.models.trafficApi.distanceMatrix;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class TrafficFragment extends Fragment {

    private TrafficViewModel trafficViewModel;
    private Button commuteBtn;
    // private EditText startingEditText;
    // private EditText destinationEditText;
    private TextView expectedTimeLabel;
    private AutocompleteSupportFragment supportFragmentStart;
    private AutocompleteSupportFragment supportFragmentEnd;
    private SupportMapFragment supportMapFragment;
    private com.ethanbovard.tapnews.LocationManager lManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trafficViewModel =
                new ViewModelProvider(this).get(TrafficViewModel.class);
        View root = inflater.inflate(R.layout.fragment_traffic, container, false);
        /*
        trafficViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        }); */
        // set up start and end AutocompleteSupportFragments
        Context context = root.getContext();

        if (!Places.isInitialized()) {
            Places.initialize(context, get(R.string.places_api_key));
        }

        supportFragmentStart = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_start);

        supportFragmentEnd = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_end);

        setAutocompleteSupportFragment(supportFragmentStart, R.string.start_commute_key, R.string.start_commute_geocode_key);
        setAutocompleteSupportFragment(supportFragmentEnd, R.string.destination_commute_key, R.string.destination_commute_geocode_key);
        supportFragmentStart.setHint("Starting address");
        supportFragmentEnd.setHint("Destination address");

        // Get distance information
        expectedTimeLabel = root.findViewById(R.id.expectedTimeLabel);
        String startCommute = get(R.string.start_commute_key);
        String destinationCommute = get(R.string.destination_commute_key);

        updateExpectedTime();

        if (startCommute.length() > 0 && destinationCommute.length() > 0) {
            supportFragmentStart.setText(get(R.string.start_commute_key));
            supportFragmentEnd.setText(get(R.string.destination_commute_key));
        }

        lManager = new com.ethanbovard.tapnews.LocationManager(getActivity());

        commuteBtn = (Button) root.findViewById(R.id.start_button);
        commuteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGoogleMaps();
            }
        });

        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        lManager.getLocationResult((MainActivity)getContext()).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    setUpMap(location);
                }
            }
        });

        return root;
    }

    public void updateExpectedTime () {
        String startCommute = get(R.string.start_commute_key);
        String destinationCommute = get(R.string.destination_commute_key);
        distanceMatrix dist = null;
        try {
            dist = (distanceMatrix) JSONManager.Deserialize(new URL(String.format("https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s",
                    startCommute, destinationCommute, get(R.string.places_api_key))), distanceMatrix.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dist != null && dist.rows.length >= 1) {
            expectedTimeLabel.setText(String.format("%s commute expected", dist.rows[0].elements[0].duration.text));
        }
    }

    public void goToGoogleMaps () {
        String startingPoint = get(R.string.start_commute_key);
        String destinationPoint = get(R.string.destination_commute_key);
        if (startingPoint.length() == 0 || destinationPoint.length() == 0) {
            Toast.makeText(getActivity(), "Please enter a starting commute address and destination address.", Toast.LENGTH_SHORT).show();
        }
        else {
            String uri = "http://maps.google.com/maps?saddr=" +startingPoint +"&daddr="+destinationPoint;
            //String uri = "http://maps.google.com/maps?q= "+destinationPoint;
            Log.v("TrafficFragment", "Opening traffic fragment...");
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }
    }
    public void setAutocompleteSupportFragment (AutocompleteSupportFragment autocompleteFragment, int id, int coord_id) {
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                Log.i("TrafficFragment", "Place: " + place.getName() + ", " + place.getId());
                String coords = place.getLatLng().latitude + "," + place.getLatLng().longitude;
                set (id, place.getName());
                set (coord_id, coords);
                updateExpectedTime();
            }
            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i("TrafficFragment", "An error occurred: " + status);
            }
        });
    }
    public String get (int key) {
        // key from R.string.(...)
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(key);
        return sharedPref.getString(getString(key), defaultValue);
    }
    public void set (int key, String value) {
        // key from R.string.(...)
        Log.i("TrafficFragment", "Saving " + value + "...");
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(key), value);
        editor.apply();
    }
    public void setUpMap (Location location) {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                googleMap.setTrafficEnabled(true);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng, 10
                        ));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
    }
}