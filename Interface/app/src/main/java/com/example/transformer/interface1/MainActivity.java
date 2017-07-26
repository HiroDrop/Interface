package com.example.transformer.interface1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        minusbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startApp();
            }
        });
    }

    private void startApp(){
        Uri uri = Uri.parse("https://www.google.co.jp/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
