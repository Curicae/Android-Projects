package com.example.sensor;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SensorManager sensorManager;
    private final Map<Integer, Integer> buttonSensorMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setupButtons();

        mapButtonsToSensors();
    }

    private void setupButtons() {
        int[] buttonIds = {
                R.id.btn_accelerometer,
                R.id.btn_compass,
                R.id.btn_gyroscope,
                R.id.btn_humidity,
                R.id.btn_light,
                R.id.btn_magnetometer,
                R.id.btn_pressure,
                R.id.btn_proximity,
                R.id.btn_thermometer
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(this);
        }
    }

    private void mapButtonsToSensors() {
        buttonSensorMap.put(R.id.btn_accelerometer, Sensor.TYPE_ACCELEROMETER);
        buttonSensorMap.put(R.id.btn_compass, Sensor.TYPE_MAGNETIC_FIELD);
        buttonSensorMap.put(R.id.btn_gyroscope, Sensor.TYPE_GYROSCOPE);
        buttonSensorMap.put(R.id.btn_humidity, Sensor.TYPE_RELATIVE_HUMIDITY);
        buttonSensorMap.put(R.id.btn_light, Sensor.TYPE_LIGHT);
        buttonSensorMap.put(R.id.btn_magnetometer, Sensor.TYPE_MAGNETIC_FIELD);
        buttonSensorMap.put(R.id.btn_pressure, Sensor.TYPE_PRESSURE);
        buttonSensorMap.put(R.id.btn_proximity, Sensor.TYPE_PROXIMITY);
        buttonSensorMap.put(R.id.btn_thermometer, Sensor.TYPE_AMBIENT_TEMPERATURE);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (buttonSensorMap.containsKey(viewId)) {
            int sensorType = buttonSensorMap.get(viewId);
            Sensor sensor = sensorManager.getDefaultSensor(sensorType);

            if (sensor != null) {
                Intent intent = new Intent(this, SensorDetailActivity.class);
                intent.putExtra("SENSOR_TYPE", sensorType);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Bu sensör cihazınızda bulunmuyor", Toast.LENGTH_SHORT).show();
            }
        }
    }
}