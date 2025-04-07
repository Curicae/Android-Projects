package com.example.hafta62;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private SeekBar seekBar;
    private EditText cityEditText;
    private Button confirmButton;
    private TextView seekBarValueText;

    private HashMap<Integer, String> plateToCity;
    private ArrayList<String> correctCities;
    private boolean gameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        seekBar = findViewById(R.id.plateSeekBar);
        cityEditText = findViewById(R.id.cityEditText);
        confirmButton = findViewById(R.id.confirmButton);
        seekBarValueText = findViewById(R.id.seekBarValueText);

        initializePlateToCity();

        correctCities = new ArrayList<>();

        seekBar.setVisibility(View.GONE);
        cityEditText.setVisibility(View.GONE);
        confirmButton.setVisibility(View.GONE);
        seekBarValueText.setVisibility(View.GONE);

        startButton.setOnClickListener(v -> {
            gameStarted = true;
            startButton.setVisibility(View.GONE);
            seekBar.setVisibility(View.VISIBLE);
            cityEditText.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.VISIBLE);
            seekBarValueText.setVisibility(View.VISIBLE);

            correctCities.clear();
        });

        seekBar.setMax(81);
        seekBar.setMin(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValueText.setText("Plaka: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        confirmButton.setOnClickListener(v -> {
            if (!gameStarted) return;

            int plateNumber = seekBar.getProgress();
            String userCity = cityEditText.getText().toString().trim();
            String correctCity = plateToCity.get(plateNumber);

            if (userCity.equalsIgnoreCase(correctCity)) {
                correctCities.add(correctCity);

                if (correctCities.size() >= 3) {
                    Toast.makeText(MainActivity.this, "Tebrikler! Oyun bitti.", Toast.LENGTH_LONG).show();
                    gameStarted = false;
                    startButton.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.GONE);
                    cityEditText.setVisibility(View.GONE);
                    confirmButton.setVisibility(View.GONE);
                    seekBarValueText.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    intent.putStringArrayListExtra("correctCities", correctCities);
                    startActivity(intent);
                }

                cityEditText.setText("");
            } else {
                Toast.makeText(MainActivity.this, "Yanlış! Tekrar deneyin.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializePlateToCity() {
        plateToCity = new HashMap<>();
        plateToCity.put(1, "Adana");
        plateToCity.put(2, "Adıyaman");
        plateToCity.put(3, "Afyonkarahisar");
        plateToCity.put(4, "Ağrı");
        plateToCity.put(5, "Amasya");
        plateToCity.put(6, "Ankara");
        plateToCity.put(7, "Antalya");
        plateToCity.put(8, "Artvin");
        plateToCity.put(9, "Aydın");
        plateToCity.put(10, "Balıkesir");
        plateToCity.put(11, "Bilecik");
        plateToCity.put(12, "Bingöl");
        plateToCity.put(13, "Bitlis");
        plateToCity.put(14, "Bolu");
        plateToCity.put(15, "Burdur");
        plateToCity.put(16, "Bursa");
        plateToCity.put(17, "Çanakkale");
        plateToCity.put(18, "Çankırı");
        plateToCity.put(19, "Çorum");
        plateToCity.put(20, "Denizli");
        plateToCity.put(21, "Diyarbakır");
        plateToCity.put(22, "Edirne");
        plateToCity.put(23, "Elazığ");
        plateToCity.put(24, "Erzincan");
        plateToCity.put(25, "Erzurum");
        plateToCity.put(26, "Eskişehir");
        plateToCity.put(27, "Gaziantep");
        plateToCity.put(28, "Giresun");
        plateToCity.put(29, "Gümüşhane");
        plateToCity.put(30, "Hakkari");
        plateToCity.put(31, "Hatay");
        plateToCity.put(32, "Isparta");
        plateToCity.put(33, "Mersin");
        plateToCity.put(34, "İstanbul");
        plateToCity.put(35, "İzmir");
        plateToCity.put(36, "Kars");
        plateToCity.put(37, "Kastamonu");
        plateToCity.put(38, "Kayseri");
        plateToCity.put(39, "Kırklareli");
        plateToCity.put(40, "Kırşehir");
        plateToCity.put(41, "Kocaeli");
        plateToCity.put(42, "Konya");
        plateToCity.put(43, "Kütahya");
        plateToCity.put(44, "Malatya");
        plateToCity.put(45, "Manisa");
        plateToCity.put(46, "Kahramanmaraş");
        plateToCity.put(47, "Mardin");
        plateToCity.put(48, "Muğla");
        plateToCity.put(49, "Muş");
        plateToCity.put(50, "Nevşehir");
        plateToCity.put(51, "Niğde");
        plateToCity.put(52, "Ordu");
        plateToCity.put(53, "Rize");
        plateToCity.put(54, "Sakarya");
        plateToCity.put(55, "Samsun");
        plateToCity.put(56, "Siirt");
        plateToCity.put(57, "Sinop");
        plateToCity.put(58, "Sivas");
        plateToCity.put(59, "Tekirdağ");
        plateToCity.put(60, "Tokat");
        plateToCity.put(61, "Trabzon");
        plateToCity.put(62, "Tunceli");
        plateToCity.put(63, "Şanlıurfa");
        plateToCity.put(64, "Uşak");
        plateToCity.put(65, "Van");
        plateToCity.put(66, "Yozgat");
        plateToCity.put(67, "Zonguldak");
        plateToCity.put(68, "Aksaray");
        plateToCity.put(69, "Bayburt");
        plateToCity.put(70, "Karaman");
        plateToCity.put(71, "Kırıkkale");
        plateToCity.put(72, "Batman");
        plateToCity.put(73, "Şırnak");
        plateToCity.put(74, "Bartın");
        plateToCity.put(75, "Ardahan");
        plateToCity.put(76, "Iğdır");
        plateToCity.put(77, "Yalova");
        plateToCity.put(78, "Karabük");
        plateToCity.put(79, "Kilis");
        plateToCity.put(80, "Osmaniye");
        plateToCity.put(81, "Düzce");
    }
}