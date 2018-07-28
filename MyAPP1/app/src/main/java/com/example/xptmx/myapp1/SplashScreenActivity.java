package com.example.xptmx.myapp1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceSate) {

        super.onCreate(savedInstanceSate);
        setContentView(R.layout.intro);

        MyGlobals.getInstance().listCreate();
        MyGlobals.getInstance().makeData(1);
        MyGlobals.getInstance().makeData(2);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}

