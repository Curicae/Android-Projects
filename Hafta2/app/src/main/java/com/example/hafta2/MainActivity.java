package com.example.hafta2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button button1 = findViewById(R.id.button4);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button button2 = findViewById(R.id.button5);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView1 = findViewById(R.id.textView2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView2 = findViewById(R.id.textView3);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editText1 = findViewById(R.id.editTextText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editText2 = findViewById(R.id.editTextText2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s1 = editText1.getText().toString();
                String s2 = editText2.getText().toString();

                int n1 = Integer.parseInt(s1);
                int n2 = Integer.parseInt(s2);

                Random random = new Random();
                int sum = random.nextInt(n2 - n1 + 1);
                String s3 = String.valueOf(sum);

                textView1.setText(s3);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s3 = textView1.getText().toString();

                if (!s3.isEmpty()) {
                    int n3 = Integer.parseInt(s3);

                    int faktoriyel = 1;
                    for (int i = 1; i <= n3; i++) {
                        faktoriyel *= i;
                    }

                    textView2.setText(String.valueOf(faktoriyel));
                }
            }
        });
    }
}
