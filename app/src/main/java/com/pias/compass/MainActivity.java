package com.pias.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView textView;
    private ImageView imageView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor, magnetometerSensor;

    private float[] lastAccelerometer= new float[3];
    private float[] lastMagnetometer= new float[3];
    private float[] rotationMatrix= new float[9];
    private float[] orientation= new float[3];

    boolean isLastAccelerometerArrayCopied= false;
    boolean isLastMagnetometerArrayCopied= false;

   long lastUpdatedTime= 0;
   float currentDegree= 0f;

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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor== accelerometerSensor){
            System.arraycopy(sensorEvent.values, 0, lastAccelerometer, 0, sensorEvent.values.length);
            isLastAccelerometerArrayCopied= true;
        } else if (sensorEvent.sensor==magnetometerSensor) {
            System.arraycopy(sensorEvent.values, 0, lastMagnetometer, 0, sensorEvent.values.length);
            isLastMagnetometerArrayCopied= true;
        }
        if (isLastAccelerometerArrayCopied && isLastMagnetometerArrayCopied && System.currentTimeMillis() - lastUpdatedTime> 360){
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix, orientation);

            float azimuthInRadians= orientation[0];
            float azimuthInDegree= (float) Math.toDegrees(azimuthInRadians);

            RotateAnimation rotateAnimation=
                    new RotateAnimation(currentDegree, -azimuthInDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(360);
            rotateAnimation.setFillAfter(true);
            imageView.startAnimation(rotateAnimation);

            currentDegree= -azimuthInDegree;
            lastUpdatedTime= System.currentTimeMillis();

            int x= (int) azimuthInDegree;
            textView.setText(x+ "°");

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this,accelerometerSensor);
        sensorManager.unregisterListener(this, magnetometerSensor);
    }
}