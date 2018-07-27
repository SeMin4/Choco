package com.example.xptmx.myapp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;


public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.setting);

        final AudioManager audioManager;
        final MediaPlayer m = MediaPlayer.create(this, R.raw.mymusic);
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton aButton = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton bButton = (RadioButton) findViewById(R.id.radioButton2);
        Switch aswitch = (Switch) findViewById(R.id.switch1);
        Switch bswitch = (Switch) findViewById(R.id.switch2);

        aswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    vibrator.vibrate(new long[]{100, 1000, 100, 500, 100, 500, 100, 1000}, 0);
                } else {
                    vibrator.cancel();
                }
            }
        });
        bswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    m.start();
                } else {
                    m.stop();

                }
            }
        });

        Boolean aa = pref.getBoolean("aButton", false);
        Boolean bb = pref.getBoolean("bButton", false);

        aButton.setChecked(aa);
        bButton.setChecked(bb);

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

                    if(audioManager.getRingerMode() == audioManager.RINGER_MODE_NORMAL) {
                        if(i == R.id.radioButton1)
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        else if(i== R.id.radioButton2)
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
                    else if(audioManager.getRingerMode() == audioManager.RINGER_MODE_VIBRATE) {
                        if (i == R.id.radioButton1)
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        else if (i == R.id.radioButton2)
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
                    else if(audioManager.getRingerMode() == audioManager.RINGER_MODE_SILENT) {
                        if (i == R.id.radioButton1)
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        else if (i == R.id.radioButton2)
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
            }
        });


        Switch cSwitch = (Switch) findViewById(R.id.alarmSwitch);

        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    startService(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    stopService(intent);
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

        editor.putBoolean("aButton", aButton.isChecked());
        editor.putBoolean("bButton", bButton.isChecked());

        editor.commit();
    }
}


