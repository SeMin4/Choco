package com.example.xptmx.myapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.setting);

        Switch aSwitch=(Switch)findViewById(R.id.alarmSwitch);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true)
                {
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    startService(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    stopService(intent);
                }
            }
        });
    }
}
