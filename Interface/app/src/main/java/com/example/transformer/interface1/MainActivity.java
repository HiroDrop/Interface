package com.example.transformer.interface1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    enum ACTION{
        INCL_RIGHT, INCL_LEFT, SHUFFLE;
    }

    Intent app1 = new Intent();

    class Action_App_Pair{
        private ACTION action;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button minusbtn = (Button)findViewById(R.id.minusbutton);

        final Spinner action = (Spinner)findViewById(R.id.app1);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


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

    private void startCamera(){
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
