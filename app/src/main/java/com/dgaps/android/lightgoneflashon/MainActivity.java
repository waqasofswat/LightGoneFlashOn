package com.dgaps.android.lightgoneflashon;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar lightMeter;
    TextView textMax, textReading;

    public static Camera cam = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lightMeter = (ProgressBar)findViewById(R.id.lightmeter);
        textMax = (TextView)findViewById(R.id.max);
        textReading = (TextView)findViewById(R.id.reading);

        SensorManager sensorManager
                = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null){
            Toast.makeText(MainActivity.this,
                    "No Light Sensor! quit-",
                    Toast.LENGTH_LONG).show();
        }else{
            float max =  lightSensor.getMaximumRange();
            lightMeter.setMax((int)max);
            textMax.setText("Max Reading: " + String.valueOf(max));

            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    SensorEventListener lightSensorEventListener
            = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if(event.sensor.getType()==Sensor.TYPE_LIGHT){
                float currentReading = event.values[0];
                lightMeter.setProgress((int)currentReading);
                textReading.setText("Current Reading: " + String.valueOf(currentReading));
                if((int)currentReading<1){
                    try {
                        if (getPackageManager().hasSystemFeature(
                                PackageManager.FEATURE_CAMERA_FLASH)) {
                            cam = Camera.open();
                            Camera.Parameters p = cam.getParameters();
                            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            cam.setParameters(p);
                            cam.startPreview();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "Exception flashLightOn()",
                                Toast.LENGTH_SHORT).show();
                    }

                }


            }
        }

    };
}