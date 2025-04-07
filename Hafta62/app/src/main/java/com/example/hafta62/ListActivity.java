package com.example.hafta62;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView cityListView = findViewById(R.id.cityListView);
        Button backButton = findViewById(R.id.backButton);

        ArrayList<String> correctCities = getIntent().getStringArrayListExtra("correctCities");

        assert correctCities != null;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                correctCities
        );
        cityListView.setAdapter(adapter);

        backButton.setOnClickListener(v -> finish());
    }
}