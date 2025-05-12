package com.example.maps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SetLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker currentMarker;
    private Button btnSaveLocation;
    private LatLng selectedLocation;
    private Button btnReturnToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Konum Belirle");
        }

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnSaveLocation = findViewById(R.id.btnSaveLocation);
        btnSaveLocation.setOnClickListener(v -> {
            if (selectedLocation != null) {
                // Here you would typically save the location to a database or preferences
                // For this example, we'll just show a toast with the coordinates
                String message = "Konum kaydedildi: " + selectedLocation.latitude 
                        + ", " + selectedLocation.longitude;
                Toast.makeText(SetLocationActivity.this, message, Toast.LENGTH_LONG).show();
                finish(); // Return to main activity
            } else {
                Toast.makeText(SetLocationActivity.this, 
                        "Lütfen önce bir konum seçin", Toast.LENGTH_SHORT).show();
            }
        });

        btnReturnToMenu = findViewById(R.id.btnReturnToMenu);
        btnReturnToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(SetLocationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Default location (Istanbul, Turkey)
        LatLng istanbul = new LatLng(41.0082, 28.9784);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(istanbul, 10f));

        // Set click listener for map
        mMap.setOnMapClickListener(latLng -> {
            // Update the marker position
            if (currentMarker != null) {
                currentMarker.remove();
            }
            
            // Add new marker
            currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Seçilen Konum"));
            
            // Save selected location
            selectedLocation = latLng;
            
            // Animate camera to the selected position
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        });
    }
} 