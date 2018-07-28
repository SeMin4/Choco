package com.example.xptmx.myapp1;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class Setting extends AppCompatActivity {
    ListView bluetoothList = null;
    public static final String BT_PREFERENCES = "BtPrefs";
    public static final String BP_PREFERENCES_PAIRED_DEVICE = "SELECTED_DEVICE"; // String
    public static boolean bluetoothcnonnected = false;
    public static String bluetooth_connectdevice;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.setting2);
        ImageButton bluebotton = (ImageButton) findViewById(R.id.set_bluebutton);
        ImageButton  simulbotton = (ImageButton) findViewById(R.id.set_simulbutton);
        bluebotton.setOnClickListener(new View.OnClickListener() {
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
}