package com.example.xptmx.myapp1;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Setting extends AppCompatActivity {

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
    long mNow;
    Date mDate;

    ListView bluetoothList = null;
    public static final String BT_PREFERENCES = "BtPrefs";
    public static final String BP_PREFERENCES_PAIRED_DEVICE = "SELECTED_DEVICE"; // String
    public static boolean bluetoothcnonnected = false;
    public static String bluetooth_connectdevice;
    BluetoothAdapter mBluetoothAdapter;
    int temp, temp2,temp3;

    final int PERMISSION=1;
    final int DIALOG_RADIO = 1;
    final int DIALOG_SWITCH = 2;
    final int DIALOG_B = 3;
    static Handler myHandler;
    list_item my_list_item;

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.setting);
        ImageButton simulbotton = (ImageButton) findViewById(R.id.set_simulbutton);
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



        listItems = getResources().getStringArray(R.array.shopping_item);
        ImageButton b3 = (ImageButton) findViewById(R.id.set_authbutton);

        checkedItems = new boolean[listItems.length];
        if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            checkedItems[0] = true;
        }
        if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            checkedItems[1] = true;
        }


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

        simulbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_list_item=new list_item(getTime(),"부산광역시 동구 남쪽 6KM 지역 규모 5.5 지진발생/여진 등 안전에 주의 바랍니다.",
                        "null","null","null");

                Message msg=myHandler.obtainMessage();
                msg.obj=my_list_item;
                myHandler.sendMessage(msg);//쓰레드에 있는 핸들러에게 메세지를 보냄


                /*MyGlobals.getInstance().makeRecentData();
                my_list_item=MyGlobals.getInstance().getRecent_list_item();

                Message msg=myHandler.obtainMessage();
                msg.obj=my_list_item;
                myHandler.sendMessage(msg);//쓰레드에 있는 핸들러에게 메세지를 보냄
                int code=MyGlobals.getInstance().makeCode(my_list_item.getContent());*/
            }
        });

        BottomNavigationView bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent0=new Intent(Setting.this,MainActivity.class);
                        startActivity(intent0);
                        return true;
                    case R.id.navigation_shelter:
                        Intent intent1=new Intent(Setting.this,Shelter.class);
                        startActivity(intent1);
                        return true;
                    case R.id.navigation_message:
                        Intent intent2=new Intent(Setting.this,Disater_message.class);
                        startActivity(intent2);
                        return true;
                    case R.id.navigation_tips:
                        Intent intent3=new Intent(Setting.this,Tips.class);
                        startActivity(intent3);
                        return true;
                    case R.id.navigation_setting:
                        return true;
                }
                return false;
            }
        });

        Menu menu=bottomNavigation.getMenu();
        MenuItem menuItem=menu.getItem(4);
        menuItem.setChecked(true);


    }
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    protected void listclick(String[] bluetoothdevice , int position, String strText){
        SharedPreferences mPairedSettings;
        mPairedSettings = getSharedPreferences(BT_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPairedSettings.edit();
        editor.putString(BP_PREFERENCES_PAIRED_DEVICE, strText);
        editor.commit();
        final String dName=strText;
        //블루투스 확인 메시지 다이얼로그 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
        builder.setCancelable(true);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Activity 에서 대화상자를 닫도록 메소드를 호출한다.

                if(!mBluetoothAdapter.isEnabled())
                {
                    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);
                }
                GlobalAduino.getInstance().setDevicename(dName);
                bluetoothcnonnected = true;

                Intent intent = new Intent(getApplicationContext(), ArduinoSerivce.class);
                startService(intent);
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
            final String str2[] = {"소리 진동", "소리", "진동","모두 끄기"};

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
                                    }else if(str2[temp2] == "모두 끄기"){
                                        m.stop();
                                        vibrator.cancel();
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
        }
        else if(id == DIALOG_B){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Setting.this);
            mBuilder.setTitle("권한설정");
            mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                    if(isChecked){
                        if(! mUserItems.contains(position)){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove(position);
                        }
                    }
                }
            });

            if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                checkedItems[0] = true;
            }
            if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                checkedItems[1] = true;
            }
            
            mBuilder.setCancelable(false);
            mBuilder.setPositiveButton("선택완료", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    if(checkedItems[1] == true && checkedItems[0] == true) {
                        if (ContextCompat.checkSelfPermission(Setting.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Setting.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Setting.this,
                                    new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);

                            if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                checkedItems[0] = false;
                            }
                            if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                checkedItems[1] = false;
                            }
                        }

                    }

                    if(checkedItems[0] == true) {
                        if (ContextCompat.checkSelfPermission(Setting.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Setting.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);
                            if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                checkedItems[0] = false;
                            }
                            else if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                checkedItems[0] = true;
                            }
                        }
                    }
                    if(checkedItems[1] == true) {
                        if (ContextCompat.checkSelfPermission(Setting.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Setting.this,
                                    new String[]{Manifest.permission.CAMERA}, PERMISSION);
                            if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                checkedItems[1] = false;
                            }
                            else if(ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                checkedItems[1] = true;
                            }
                        }
                    }

                    String item = "";
                    for(int i=0;i<mUserItems.size(); i++){
                        item = item + listItems[mUserItems.get(i)];
                        if(i != mUserItems.size()-1){
                            item = item + ", ";
                        }
                    }

                }
            });
            mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });


            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        }
        return super.onCreateDialog(id);
    }



    protected static void setHandler(Handler handler){
        myHandler=handler;
    }
}