package com.example.hafta4_3;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final String city = getIntent().getStringExtra("city");
        final int selectedPlate = getIntent().getIntExtra("selectedPlate", -1);
        final int correctPlate = getIntent().getIntExtra("correctPlate", -1);
        final int index = getIntent().getIntExtra("index", -1);

        TextView resultTextView = findViewById(R.id.resultTextView);

        boolean isCorrect = selectedPlate == correctPlate;

        if (isCorrect) {
            Toast.makeText(this, "Doğru", Toast.LENGTH_SHORT).show();
            resultTextView.setText("Tebrikler! " + city + " şehri için doğru plakayı seçtiniz: " +
                    (selectedPlate < 10 ? "0" + selectedPlate : selectedPlate));
        } else {
            Toast.makeText(this, "Yanlış. Doğru plaka: " +
                    (correctPlate < 10 ? "0" + correctPlate : correctPlate), Toast.LENGTH_SHORT).show();
            resultTextView.setText(" " + city + " şehrinin plakası yanlış.\n" +
                    "Doğru plaka: " + (correctPlate < 10 ? "0" + correctPlate : correctPlate));
        }

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("index", index);
            resultIntent.putExtra("correctPlate", correctPlate);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
