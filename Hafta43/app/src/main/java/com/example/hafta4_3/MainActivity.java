package com.example.hafta4_3;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final Map<String, Integer> cities = new HashMap<>() {{
        put("İstanbul", 34);
        put("Ankara", 6);
        put("İzmir", 35);
        put("Bursa", 16);
        put("Antalya", 7);
        put("Rize", 53);
        put("Samsun", 55);
        put("Trabzon", 61);
        put("Eskişehir", 26);
        put("Gaziantep", 27);
    }};

    private List<String> selectedCities;
    private List<Integer> displayedPlates;

    private List<TextView> plateTextViews;
    private List<TextView> cityTextViews;

    private static final int CHECK_ANSWER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        prepareGameData();

        updateUI();
    }

    private void initializeViews() {
        plateTextViews = new ArrayList<>();
        plateTextViews.add(findViewById(R.id.plate1));
        plateTextViews.add(findViewById(R.id.plate2));
        plateTextViews.add(findViewById(R.id.plate3));
        plateTextViews.add(findViewById(R.id.plate4));
        plateTextViews.add(findViewById(R.id.plate5));
        plateTextViews.add(findViewById(R.id.plate6));
        plateTextViews.add(findViewById(R.id.plate7));
        plateTextViews.add(findViewById(R.id.plate8));
        plateTextViews.add(findViewById(R.id.plate9));
        plateTextViews.add(findViewById(R.id.plate10));

        cityTextViews = new ArrayList<>();
        cityTextViews.add(findViewById(R.id.city1));
        cityTextViews.add(findViewById(R.id.city2));
        cityTextViews.add(findViewById(R.id.city3));
        cityTextViews.add(findViewById(R.id.city4));
        cityTextViews.add(findViewById(R.id.city5));
        cityTextViews.add(findViewById(R.id.city6));
        cityTextViews.add(findViewById(R.id.city7));
        cityTextViews.add(findViewById(R.id.city8));
        cityTextViews.add(findViewById(R.id.city9));
        cityTextViews.add(findViewById(R.id.city10));

        List<Button> checkButtons = new ArrayList<>();
        checkButtons.add(findViewById(R.id.check1));
        checkButtons.add(findViewById(R.id.check2));
        checkButtons.add(findViewById(R.id.check3));
        checkButtons.add(findViewById(R.id.check4));
        checkButtons.add(findViewById(R.id.check5));
        checkButtons.add(findViewById(R.id.check6));
        checkButtons.add(findViewById(R.id.check7));
        checkButtons.add(findViewById(R.id.check8));
        checkButtons.add(findViewById(R.id.check9));
        checkButtons.add(findViewById(R.id.check10));

        for (int i = 0; i < checkButtons.size(); i++) {
            final int index = i;
            checkButtons.get(i).setOnClickListener(v -> {
                String city = selectedCities.get(index);
                int selectedPlate = displayedPlates.get(index);
                int correctPlate = cities.get(city);

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("city", city);
                intent.putExtra("selectedPlate", selectedPlate);
                intent.putExtra("correctPlate", correctPlate);
                intent.putExtra("index", index);
                startActivityForResult(intent, CHECK_ANSWER_REQUEST);
            });
        }
    }

    private void prepareGameData() {
        List<String> cityNames = new ArrayList<>(cities.keySet());
        Collections.shuffle(cityNames);
        selectedCities = cityNames.subList(0, 10);

        List<Integer> allPlates = new ArrayList<>();
        for (int i = 1; i <= 81; i++) {
            allPlates.add(i);
        }
        Collections.shuffle(allPlates);
        displayedPlates = allPlates.subList(0, 10);
    }

    private void updateUI() {
        for (int i = 0; i < 10; i++) {
            int plateNumber = displayedPlates.get(i);
            String formattedPlate = plateNumber < 10 ? "0" + plateNumber : String.valueOf(plateNumber);
            plateTextViews.get(i).setText(formattedPlate);

            cityTextViews.get(i).setText(selectedCities.get(i));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHECK_ANSWER_REQUEST && resultCode == RESULT_OK) {
            int index = data.getIntExtra("index", -1);
            int correctPlate = data.getIntExtra("correctPlate", -1);

            if (index >= 0 && index < displayedPlates.size()) {
                displayedPlates.set(index, correctPlate);

                String formattedPlate = correctPlate < 10 ? "0" + correctPlate : String.valueOf(correctPlate);
                plateTextViews.get(index).setText(formattedPlate);

                plateTextViews.get(index).setBackgroundResource(android.R.color.holo_green_light);
                cityTextViews.get(index).setBackgroundResource(android.R.color.holo_green_light);
            }
        }
    }
}
