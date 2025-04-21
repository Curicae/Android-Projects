package com.example.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SensorDetailActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView tvSensorName;
    private TextView tvSensorValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);

        tvSensorName = findViewById(R.id.tv_sensor_name);
        tvSensorValues = findViewById(R.id.tv_sensor_values);

        // Sensör yöneticisini al
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Intent'ten sensör tipini al
        int sensorType = getIntent().getIntExtra("SENSOR_TYPE", Sensor.TYPE_ACCELEROMETER);

        // Sensör nesnesini al
        sensor = sensorManager.getDefaultSensor(sensorType);

        if (sensor != null) {
            // Sensör adını ayarla
            tvSensorName.setText(sensor.getName());
        } else {
            tvSensorName.setText("Sensör bulunamadı");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            // Sensör olaylarını dinlemeye başla
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Aktivite arka plana geçtiğinde sensör dinlemeyi durdur
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Sensör değerlerini ekrana yaz
        StringBuilder values = new StringBuilder();
        for (int i = 0; i < event.values.length; i++) {
            values.append("Değer ").append(i + 1).append(": ").append(event.values[i]).append("\n");
        }
        tvSensorValues.setText(values.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Sensör doğruluğu değiştiğinde yapılacak işlemler (burada kullanılmıyor)
    }
}