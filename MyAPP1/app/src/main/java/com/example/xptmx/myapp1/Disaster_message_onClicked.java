package com.example.xptmx.myapp1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Disaster_message_onClicked extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster_message_on_clicked);

        Intent intent = getIntent();
        String name = intent.getStringExtra("msg");

        TextView tv_1=(TextView)findViewById(R.id.tv_1);
        TextView tv_2=(TextView)findViewById(R.id.tv_2);
        TextView tv_3=(TextView)findViewById(R.id.tv_3);
        tv_1.setText(name);
        tv_2.setText("송출지역 : " + intent.getExtras().getString("location_name"));
        tv_3.setText(intent.getExtras().getString("create_date"));
    }
}