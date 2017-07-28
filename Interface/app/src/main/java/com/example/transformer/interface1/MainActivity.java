package com.example.transformer.interface1;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.transformer.interface1.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button minusbtn = (Button)findViewById(R.id.minusbutton);

        final Spinner action = (Spinner)findViewById(R.id.app1);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

<<<<<<< HEAD
=======

>>>>>>> 327ad5565fba2407e53dcbabae47a71e40b52cd4
        minusbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String item = (String)action.getSelectedItem();

                if(item.equals("カメラ")){
                    startCamera();
                }
                else if(item.equals("ブラウザ")){
                    startBrowser();
                }
                else if(item.equals("ダイアル")){
                    startDial();
                }
                else{
                    builder.setMessage("アプリを選んでください.");
                    builder.show();
                }
            }
        });
    }

<<<<<<< HEAD
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){}

    `@Override
    public void onSensorChanged(SensorEvent event){
        switch(event.sensor.getType()){
            case Sensor.TYPE_GYROSCOPE:
                break;
            case Sensor.TYPE_ACCELEROMETER:
                break;
        }
    }

    private void startCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }
    private void startDial(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        startActivity(intent);
    }
    private void startBrowser(){
=======
    private void startCamera(){
>>>>>>> 327ad5565fba2407e53dcbabae47a71e40b52cd4
        Uri uri = Uri.parse("https://www.google.co.jp/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    private void startDial(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        startActivity(intent);
    }
    private void startBrowser(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

}