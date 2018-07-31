package com.example.xptmx.myapp1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

        BottomNavigationView bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent0=new Intent(Tips.this,MainActivity.class);
                        startActivity(intent0);
                        return true;
                    case R.id.navigation_shelter:
                        Intent intent1=new Intent(Tips.this,Shelter.class);
                        startActivity(intent1);
                        return true;
                    case R.id.navigation_message:
                        Intent intent2=new Intent(Tips.this,Disater_message.class);
                        startActivity(intent2);
                        return true;
                    case R.id.navigation_tips:
                        return true;
                    case R.id.navigation_setting:
                        Intent intent4=new Intent(Tips.this,Setting.class);
                        startActivity(intent4);
                        return true;
                }
                return false;
            }
        });

        Menu menu=bottomNavigation.getMenu();
        MenuItem menuItem=menu.getItem(3);
        menuItem.setChecked(true);

    }
}