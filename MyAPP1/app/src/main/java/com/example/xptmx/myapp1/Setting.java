package com.example.xptmx.myapp1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;


public class Setting extends AppCompatActivity {
    int temp;

    final int DIALOG_RADIO = 1;
    final int DIALOG_SWITCH = 2;
    final int DIALOG_B =3;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.setting2);

        ImageButton b1 = (ImageButton) findViewById(R.id.set_alarmbutton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_RADIO);
            }
        });
        ImageButton b2 = (ImageButton) findViewById(R.id.set_emerbutton);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_SWITCH);
            }
        });
        ImageButton b3 = (ImageButton)findViewById(R.id.set_authbutton);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_B);
            }
        });
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        final MediaPlayer m = MediaPlayer.create(this, R.raw.aa);
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Switch aswitch = (Switch) findViewById(R.id.emer_switch1);
        Switch bswitch = (Switch) findViewById(R.id.emer_switch2);

        final AudioManager audioManager;
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        Log.d("test", "onCreateDialog");
        if (id == DIALOG_RADIO) {
            AlertDialog.Builder builder1 =
                    new AlertDialog.Builder(Setting.this);
            final String str1[] = {"소리", "진동"};
            builder1.setTitle("알림설정")
                    .setPositiveButton("선택완료",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(str1[temp] == "소리"){
                                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                        Toast.makeText(getApplicationContext(),
                                                str1[temp] + "를 선택했음",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else if(str1[temp] =="진동"){
                                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                        Toast.makeText(getApplicationContext(),
                                                str1[temp] + "을 선택했음",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                    .setNegativeButton("취소", null)
                    .setSingleChoiceItems
                            (str1,
                                    -1,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            temp = which;
                                        }
                                    });
            return builder1.create();
        }
        else if(id == DIALOG_SWITCH){
            AlertDialog.Builder emer = new AlertDialog.Builder(Setting.this);
            LayoutInflater inflater2 = this.getLayoutInflater();
            View dialogView1 = inflater2.inflate(R.layout.emer_switch, null);
            emer.setView(dialogView1);
            emer.setNegativeButton("닫기", null);
            emer.setTitle("위급상황 시");
           /* aswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        vibrator.vibrate(new long[]{100, 1000, 100, 500, 100,500,100,1000},0);
                    }
                    else{
                        vibrator.cancel();
                    }
                }
            });
            bswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        m.start();
                    }
                    else{
                        m.stop();
                    }
                }
            });*/
            emer.create().show();

        }
        else if(id == DIALOG_B){
            AlertDialog.Builder auth = new AlertDialog.Builder(this);
            LayoutInflater inflater3 = this.getLayoutInflater();
            View dialogView1 = inflater3.inflate(R.layout.auth_switch, null);
            auth.setView(dialogView1);
            auth.setTitle("권한설정");
            auth.setNegativeButton("닫기", null);
            auth.create().show();
        }
        return super.onCreateDialog(id);
    }
}
