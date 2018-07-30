package com.example.xptmx.myapp1;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Setting extends AppCompatActivity {
    ListView bluetoothList = null;
    public static final String BT_PREFERENCES = "BtPrefs";
    public static final String BP_PREFERENCES_PAIRED_DEVICE = "SELECTED_DEVICE"; // String
    public static boolean bluetoothcnonnected = false;
    public static String bluetooth_connectdevice;
    BluetoothAdapter mBluetoothAdapter;
    int temp, temp2;

    final int DIALOG_RADIO = 1;
    final int DIALOG_SWITCH = 2;
    final int DIALOG_B = 3;



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.setting);
        ImageButton  simulbotton = (ImageButton) findViewById(R.id.set_simulbutton);
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
        ImageButton b3 = (ImageButton) findViewById(R.id.set_authbutton);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_B);
            }
        });
        ImageButton bluebutton = (ImageButton) findViewById(R.id.set_bluebutton);
        bluebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    final String[] bluetoothdevice = new String[pairedDevices.size()];
                    int i = 0;
                    for (BluetoothDevice device : pairedDevices) {
                        bluetoothdevice[i++] = device.getName();
                    }
                    bluetoothList = new ListView(Setting.this);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Setting.this, R.layout.bluetooth_list, R.id.bluetoothdevice, bluetoothdevice);
                    bluetoothList.setAdapter(adapter);
                    bluetoothList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            String strText = bluetoothdevice[position];
                            if(bluetoothcnonnected == false){
                                listclick(bluetoothdevice, position,strText);
                                bluetooth_connectdevice = strText;
                            }
                            else if(bluetoothcnonnected == true){
                                if(bluetooth_connectdevice == strText)
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("OK", null);
                                    builder.setMessage(strText + "에 이미 연결 되어있습니다.");
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                                else
                                {
                                    listclick(bluetoothdevice, position,strText);
                                    bluetooth_connectdevice = strText;
                                }
                            }
                        }
                    });
                    AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                    builder.setCancelable(true);
                    builder.setView(bluetoothList);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Activity 에서 대화상자를 닫도록 메소드를 호출한다.
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    protected void listclick(String[] bluetoothdevice , int position, String strText){
        SharedPreferences mPairedSettings;
        mPairedSettings = getSharedPreferences(BT_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPairedSettings.edit();
        editor.putString(BP_PREFERENCES_PAIRED_DEVICE, strText);
        editor.commit();
        //블루투스 확인 메시지 다이얼로그 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
        builder.setCancelable(true);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Activity 에서 대화상자를 닫도록 메소드를 호출한다.
                Intent intent = new Intent(getApplicationContext(), ControlArduino.class);
                startActivity(intent);
                bluetoothcnonnected = true;
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Activity 에서 대화상자를 닫도록 메소드를 호출한다.
                dialog.cancel();
            }
        });
        builder.setTitle("확인");
        builder.setMessage(strText + "에 블루투스 연결을 하겠습니까?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {

        final MediaPlayer m = MediaPlayer.create(Setting.this, R.raw.aa);
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
                                    Toast.makeText(getApplicationContext(),str1[temp] + "을 선택했음", Toast.LENGTH_SHORT).show();
                                    if (str1[temp] == "소리") {
                                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                    } else if (str1[temp] == "진동") {
                                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
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
        } else if (id == DIALOG_SWITCH) {
            AlertDialog.Builder builder2 =
                    new AlertDialog.Builder(Setting.this);
            final String str2[] = {"소리 진동", "소리", "진동"};

            builder2.setTitle("위급 상황 시")
                    .setPositiveButton("선택완료",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    vibrator.cancel();
                                    Toast.makeText(getApplicationContext(),
                                            str2[temp2] + "을 선택했음",
                                            Toast.LENGTH_SHORT).show();
                                    if (str2[temp2] == "소리") {
                                        m.start();
                                    } else if (str2[temp2] == "소리 진동") {
                                        m.start();
                                        vibrator.vibrate(new long[]{100, 1000, 100, 500, 100, 500, 100, 1000}, 0);
                                    } else if (str2[temp2] == "진동") {
                                        m.stop();
                                        vibrator.vibrate(new long[]{100, 1000, 100, 500, 100, 500, 100, 1000}, 0);
                                    }
                                }

                            })
                    .setNegativeButton("취소", null)
                    .setSingleChoiceItems
                            (str2,
                                    -1,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            temp2 = which;
                                        }
                                    });
            return builder2.create();
        } else if (id == DIALOG_B) {
            AlertDialog.Builder auth = new AlertDialog.Builder(this);
            LayoutInflater inflater3 = this.getLayoutInflater();
            View dialogView1 = inflater3.inflate(R.layout.auth_switch, null);
            auth.setView(dialogView1);
            auth.setTitle("권한설정");
            auth.create().show();
        }
        return super.onCreateDialog(id);
    }
}


