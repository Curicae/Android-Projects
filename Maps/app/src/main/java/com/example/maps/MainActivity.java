package com.example.maps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnCurrentLocation, btnSetLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
        btnSetLocation = findViewById(R.id.btnSetLocation);

        btnCurrentLocation.setOnClickListener(v ->
            startActivity(new Intent(MainActivity.this, CurrentLocationActivity.class)));

        btnSetLocation.setOnClickListener(v ->
            startActivity(new Intent(MainActivity.this, SetLocationActivity.class)));
    }
}