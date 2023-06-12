package com.pias.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor, magnetometerSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textView= findViewById(R.id.textview);
        imageView= findViewById(R.id.imageview);

        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor= sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
}