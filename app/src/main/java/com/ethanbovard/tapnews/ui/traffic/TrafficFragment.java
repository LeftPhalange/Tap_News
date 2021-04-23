package com.ethanbovard.tapnews.ui.traffic;

import android.content.Intent;
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

import com.ethanbovard.tapnews.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class TrafficFragment extends Fragment {

    private TrafficViewModel trafficViewModel;
    private Button commuteBtn;
    private EditText startingEditText;
    private EditText destinationEditText;
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

        destinationEditText = root.findViewById(R.id.editTextDestination);
        startingEditText = root.findViewById(R.id.editTextStarting);

        lManager = new com.ethanbovard.tapnews.LocationManager(getActivity());

        commuteBtn = (Button) root.findViewById(R.id.start_button);
        commuteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startingPoint = startingEditText.getText().toString();
                String destinationPoint = destinationEditText.getText().toString();
                if (startingEditText.getText().length() == 0 || destinationEditText.getText().length() == 0) {
                    Toast.makeText(root.getContext(), "Please enter a starting commute address and destination address.", Toast.LENGTH_SHORT).show();
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
        });

        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        lManager.getLocationResult().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    setUpMap(location);
                }
            }
        });

        return root;
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