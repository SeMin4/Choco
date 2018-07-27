package com.example.xptmx.myapp1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Tips extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.tips);

        ImageButton button1 = (ImageButton) findViewById(R.id.imageButton2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safekorea.go.kr/idsiSFK/127/menuMap.do?w2xPath=/idsiSFK/wq/sfk/cs/contents/prevent/SDIJKM5116.xml"));
                startActivity(intent);
            }
        });

        ImageButton button2 = (ImageButton) findViewById(R.id.imageButton3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safekorea.go.kr/idsiSFK/126/menuMap.do?w2xPath=/idsiSFK/wq/sfk/cs/contents/prevent/prevent09.xml"));
                startActivity(intent);
            }
        });

        ImageButton button3 = (ImageButton) findViewById(R.id.imageButton4);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safekorea.go.kr/idsiSFK/126/menuMap.do?w2xPath=/idsiSFK/wq/sfk/cs/contents/prevent/prevent07.xml"));
                startActivity(intent);
            }
        });

        ImageButton button4 = (ImageButton) findViewById(R.id.imageButton5);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safekorea.go.kr/idsiSFK/126/menuMap.do?w2xPath=/idsiSFK/wq/sfk/cs/contents/prevent/prevent05.xml"));
                startActivity(intent);
            }
        });

        ImageButton button5 = (ImageButton) findViewById(R.id.imageButton6);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safekorea.go.kr/idsiSFK/126/menuMap.do?w2xPath=/idsiSFK/wq/sfk/cs/contents/prevent/prevent03.xml"));
                startActivity(intent);
            }
        });

        ImageButton button6 = (ImageButton) findViewById(R.id.imageButton7);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.safekorea.go.kr/idsiSFK/126/menuMap.do?w2xPath=/idsiSFK/wq/sfk/cs/contents/prevent/prevent10.xml"));
                startActivity(intent);
            }
        });
    }
}