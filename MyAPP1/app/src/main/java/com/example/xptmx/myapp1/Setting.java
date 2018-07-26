package com.example.xptmx.myapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class Setting extends AppCompatActivity {

    Switch aswitch, bswitch;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.setting);

        final MediaPlayer m = MediaPlayer.create(this, R.raw.t);
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton aButton = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton bButton = (RadioButton) findViewById(R.id.radioButton2);
        aswitch = (Switch) findViewById(R.id.switch1);
        bswitch = (Switch) findViewById(R.id.switch2);

        aswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    vibrator.vibrate(new long[]{100,1000,100,500,100,500,100,1000},0);
                }
                else {
                    vibrator.cancel();
                }
            }
        });
        bswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    m.start();
                }
                else{
                    m.stop();

                }
            }
        });

        Boolean aa = pref.getBoolean("aButton", false);
        Boolean bb = pref.getBoolean("bButton", false);
        Boolean dd = pref.getBoolean("aswitch", false);
        Boolean ee = pref.getBoolean("bswitch", false);

        aButton.setChecked(aa);
        bButton.setChecked(bb);
        aswitch.setChecked(dd);
        bswitch.setChecked(ee);

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton1:
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        break;
                    case R.id.radioButton2:
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        break;
                }
            }
        });

    }
    public void onStop() {
        super.onStop();
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        RadioButton aButton = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton bButton = (RadioButton) findViewById(R.id.radioButton2);
        aswitch = (Switch) findViewById(R.id.switch1);
        bswitch = (Switch) findViewById(R.id.switch2);

        editor.putBoolean("aButton", aButton.isChecked());
        editor.putBoolean("bButton", bButton.isChecked());
        editor.putBoolean("aswitch", aswitch.isChecked());
        editor.putBoolean("bswitch", bswitch.isChecked());

        editor.commit();

        Switch cSwitch=(Switch)findViewById(R.id.alarmSwitch);

        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
