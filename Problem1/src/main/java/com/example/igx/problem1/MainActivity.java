package com.example.igx.problem1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.id.message;

public class MainActivity extends AppCompatActivity implements SensorEventListener /* implements Something1, Something2 */ {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mLinear;
    private Sensor mGravity;

    String str_loc = "";
    String str_accel = "";
    String str_gyro = "";
    String str_lin = "";
    String str_grav = "";

    int sensor = 0;

    TextView text_selectedData;
    TextView text_selectedType;
    EditText edit_phoneNumber;

    final int APP_PERMISSIONS_GRANTED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLinear = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        Button btn_getLocation = (Button) findViewById(R.id.btn_getLocation);
        Button btn_getSensors = (Button) findViewById(R.id.btn_getSensors);
        Button btn_sendMessage = (Button) findViewById(R.id.btn_sendMessage);

        text_selectedData = (TextView) findViewById(R.id.text_selectedData);
        text_selectedType = (TextView) findViewById(R.id.text_selectedType);
        edit_phoneNumber = (EditText) findViewById(R.id.edit_phoneNumber);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, APP_PERMISSIONS_GRANTED);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, APP_PERMISSIONS_GRANTED);
            }
        } else {

        }

        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensor = 0;
                text_selectedType.setText("LOCATION");
                text_selectedData.setText(text_selectedType.getText().toString());
            }
        });

        btn_getSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensor = 1;
                text_selectedType.setText("SENSORS");
                text_selectedData.setText(str_accel + " / " + str_gyro + " / " + str_lin + " / " + str_grav);
            }
        });

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + edit_phoneNumber.getText().toString()));
                intent.putExtra(text_selectedData.getText().toString(), message);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLinear, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 아무 동작 안함
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_GYROSCOPE:
                str_gyro = "GYROSCOPE : " + event.values[0] + " " + event.values[1] + " " + event.values[2];
                break;

            case Sensor.TYPE_ACCELEROMETER:
                str_accel = "ACCELEROMETER : " + event.values[0] + " " + event.values[1] + " " + event.values[2];
                break;

            case Sensor.TYPE_GRAVITY:
                str_grav = "GRAVITY : " + event.values[0] + " " + event.values[1] + " " + event.values[2];
                break;

            case Sensor.TYPE_LINEAR_ACCELERATION:
                str_lin = "LINEAR_ACCELERATION : " + event.values[0] +" " + event.values[1] + " " + event.values[2];
                break;
        }

        if (sensor == 1) {
            text_selectedData.setText(str_accel + " / " + str_gyro + " / " + str_lin + " / " + str_grav);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case APP_PERMISSIONS_GRANTED : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 동의 버튼 선택", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "권한 사용을 동의해야 이용가능", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
