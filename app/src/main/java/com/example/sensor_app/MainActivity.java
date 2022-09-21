package com.example.sensor_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SensorManager sm = null;
    EditText txtX = null;
    EditText txtY = null;
    EditText txtZ = null;
    List list;
    float[] value;
    SensorEventListener sel;
    CameraManager cameraM;
    String getCameraID;
    String number;
    Intent call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText txtNumber = this.findViewById(R.id.txtNumber);
        Button add = this.findViewById(R.id.btnAdd);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = txtNumber.getText().toString();
                txtNumber.setEnabled(false);
                call = new Intent();
                call.setAction(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:"+number));
            }
        });

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        list = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

        txtX = this.findViewById(R.id.txtX);
        txtY = this.findViewById(R.id.txtY);
        txtZ = this.findViewById(R.id.txtZ);

        sel = new SensorEventListener(){
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            public void onSensorChanged(SensorEvent event) {
                value = event.values;
//                txtX.setText(Double.toString(Math.round(value[0])));
//                txtY.setText(Double.toString(Math.round(value[1])));
//                txtZ.setText(Double.toString(Math.round(value[2])));
                txtX.setText(": "+value[0]);
                txtY.setText(": "+value[1]);
                txtZ.setText(": "+value[2]);

                cameraM = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                    getCameraID = cameraM.getCameraIdList()[0];
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                if(event.values[1] > 5){
                    try {
                        cameraM.setTorchMode(getCameraID, true);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        cameraM.setTorchMode(getCameraID,false);
                    }catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }

                if (event.values[2] > 20){
                    startActivity(call);
                }
            }
        };
        sm.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void add(View v){


    }
}