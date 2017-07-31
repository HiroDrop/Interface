package com.example.transformer.interface1;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.transformer.interface1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.example.transformer.interface1.MainActivity.MOTION.INCL_LEFT;
import static com.example.transformer.interface1.MainActivity.MOTION.INCL_RIGHT;
import static com.example.transformer.interface1.MainActivity.MOTION.SHUFFLE;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private final int MOTION_NUM = 3;

    //地磁気センサ用
    float[] magnetic = null;
    float[] gravity = null;
    private boolean signed = false;
    private float sensorX = 0.0f;
    private float sensorY = 0.0f;
    private final float th = 45.0f;

    //加速度センサ用
    private long lastforce = 0;
    private long lasttime = 0;
    private long lastshake = 0;
    private int shakecount = 0;
    private float lastaccel[] = new float[3];
    private final int SHAKETIMEOUT = 1000;
    private final float FORCETHRESHOLD = 1000.0f;
    private final int TIMETHRESHOLD = 100;
    private final int SHAKECOUNT = 3;
    private final int SHAKEDURATION = 100;

    //アプリが起動状態かどうか
    private boolean started[] = new boolean[MOTION_NUM];

    //モーションの列挙
    enum MOTION{
        INCL_RIGHT, INCL_LEFT, SHUFFLE;
    }

    //モーションと文字列の対応
    private final Map<MOTION, String> motionmap = new HashMap<MOTION, String>(){
        {
            put(INCL_RIGHT, "右に傾ける");
            put(INCL_LEFT, "左に傾ける");
            put(SHUFFLE, "振る");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        for(int i = 0; i < MOTION_NUM; i++) started[i] = false;
        lastaccel[0] = lastaccel[1] = lastaccel[2] = -1.0f;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Log.i("情報", "準備完了");

//        Button minusbtn = (Button)findViewById(R.id.minusbutton);
//        final Spinner action = (Spinner)findViewById(R.id.app1);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        minusbtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                String item = (String)action.getSelectedItem();
//
//                if(item.equals("カメラ")){
//                    startCamera();
//                }
//                else if(item.equals("ブラウザ")){
//                    startBrowser();
//                }
//                else if(item.equals("ダイアル")){
//                    startDial();
//                }
//                else{
//                    builder.setMessage("アプリを選んでください.");
//                    builder.show();
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //ジャイロセンサー取得
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
        Log.i("初期化", String.valueOf(sensors.size()));
        if(sensors.size() > 0){
            Sensor s = sensors.get(0);
            mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }

        //地磁気センサー取得
        sensors = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        Log.i("初期化", String.valueOf(sensors.size()));
        if(sensors.size() > 0){
            Sensor s = sensors.get(0);
            mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
            Log.i("初期化", "added magnetic sensor");
        }

        //加速度センサー取得
        sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size() > 0){
            Sensor s = sensors.get(0);
            mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

//    @Override
//    protected void onPause(){
//        super.onPause();
//        mSensorManager.unregisterListener(this);
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){}

    @Override
    public void onSensorChanged(SensorEvent event){ //センサーのデータに変化があれば
        switch(event.sensor.getType()){
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetic = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER: //加速度センサーについて
                gravity = event.values.clone();
                long now = System.currentTimeMillis();
                if((now - lastforce) > SHAKETIMEOUT) shakecount = 0;
                if((now - lasttime) > TIMETHRESHOLD){
                    long diff = now - lasttime;
                    float speed = Math.abs(event.values[0] + event.values[1] + event.values[2] - lastaccel[0] - lastaccel[1] - lastaccel[2]) / diff * 10000;
                    if(speed > FORCETHRESHOLD){
                        if((++shakecount > SHAKECOUNT) && now - lastshake > SHAKEDURATION){
                            Log.i("motion", "shuffled!");
                            startApp(MOTION.SHUFFLE);      //振ったらアプリ起動
                            lastshake = now;
                            shakecount = 0;
                        }
                        lastforce = now;
                    }
                    lasttime = now;
                    lastaccel[0] = event.values[0];
                    lastaccel[1] = event.values[1];
                    lastaccel[2] = event.values[2];
                }
                break;
        }
        if(magnetic != null && gravity != null){
            float[] inR = new float[9];
            float[] outR = new float[9];
            float[] attitude = new float[3];
            mSensorManager.getRotationMatrix(inR, null, gravity, magnetic);
            mSensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Y, outR);
            mSensorManager.getOrientation(outR, attitude);

            float RAD2DEG = (float)( 180.0f / Math.PI);
            sensorX = (float)(attitude[1] * RAD2DEG);
            sensorY = (float)(attitude[2] * RAD2DEG);
            TextView t = (TextView)findViewById(R.id.sensorY);
            t.setText(String.valueOf(sensorY));
            if(sensorX > -90.0f) {
                if (sensorY > th){
                    Log.i("motion", "right");
                    startApp(INCL_RIGHT);
                }
                else if (sensorY < -1.0f * th){
                    Log.i("motion", "left");
                    startApp(INCL_LEFT);
                }
            }
        }
    }

    private void startApp(MOTION type){
        Spinner mlist[] = new Spinner[MOTION_NUM];  //モーション用配列
        Spinner alist[] = new Spinner[MOTION_NUM];  //アプリ用配列

        //初期化処理
        mlist[0] = (Spinner) findViewById(R.id.action1);
        mlist[1] = (Spinner) findViewById(R.id.action2);
        mlist[2] = (Spinner) findViewById(R.id.action3);
        alist[0] = (Spinner) findViewById(R.id.app1);
        alist[1] = (Spinner) findViewById(R.id.app2);
        alist[2] = (Spinner) findViewById(R.id.app3);
        String motion = motionmap.get(type);

        //設定されているモーションがあるかどうか検索
        for(int i = 0; i < MOTION_NUM; i++){
            if(!started[i] && mlist[i].getSelectedItem().equals(motion)){
                String item = (String) alist[i].getSelectedItem();
                if (item.equals("カメラ")) {
                    startCamera();
//                    started[i] = true;
                    break;
                } else if (item.equals("ブラウザ")) {
                    startBrowser();
//                    started[i] = true;
                    break;
                } else if (item.equals("ダイアル")) {
                    startDial();
//                    started[i] = true;
                }
                else continue;
                break;
            }
        }
    }

    //カメラ起動
    private void startCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        startActivity(intent);
    }

    //ダイアル起動
    private void startDial(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        startActivity(intent);
    }

    //ブラウザ起動
    private void startBrowser(){
        Uri uri = Uri.parse("https://www.google.co.jp/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        startActivity(intent);
    }
}