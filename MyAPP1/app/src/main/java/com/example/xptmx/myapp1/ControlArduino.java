package com.example.xptmx.myapp1;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class ControlArduino extends AppCompatActivity {

    public static final String BT_PREFERENCES = "BtPrefs";
    public static final String BP_PREFERENCES_PAIRED_DEVICE = "SELECTED_DEVICE"; // String
    BluetoothAdapter mBluetoothAdapter;
    //블루투스 오픈 사용
    BluetoothSocket mmSocket;
    //블루투스 기기
    BluetoothDevice mmDevice;
    //블루투스로 전송시 사용
    OutputStream mmOutputStream;
    //블루투스로부터 데이트 받을때 사용
    InputStream mmInputStream;

    boolean is_connected = false;
    String devicename;

    //블루투스로부터 들어오는 데이타 처리위한 것들
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    Button openButton;
    Button closeButton;
    Button sendButton;
    TextView info_textview;
    EditText myTextbox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_arduino);
        //이전 화면에서 선택하고 저장한  블루투스 이름 찾기
       SharedPreferences mPairedSettings;
        mPairedSettings = getSharedPreferences(BT_PREFERENCES, Context.MODE_PRIVATE);
        if (!mPairedSettings.contains(BP_PREFERENCES_PAIRED_DEVICE)) {
            // 선택하지 않았다면  종료 한다.
            // 사실 선택하지 않으면 이 화면으로 오지 않는다
        }
        devicename = mPairedSettings.getString(BP_PREFERENCES_PAIRED_DEVICE, "");
        info_textview = (TextView) findViewById(R.id.textview_info);
        //openButton = (Button)findViewById(R.id.open);
        closeButton = (Button)findViewById(R.id.close);
        sendButton = (Button)findViewById(R.id.send);
        myTextbox = (EditText)findViewById(R.id.EditText_bt_data);
        try
        {
            findBT();
            openBT();
            //연결후 연결 해제 버튼은 enable , 연결 버튼은 disable
            //openButton.setEnabled(false);
            //closeButton.setEnabled(true);
        }
        catch (IOException ex) {
        }

        //openButton.setFocusableInTouchMode(true);
        //openButton.requestFocus();
        //Open BT Button
        /*openButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    findBT();
                    openBT();

                    //연결후 연결 해제 버튼은 enable , 연결 버튼은 disable
                    openButton.setEnabled(false);
                    closeButton.setEnabled(true);

                }
                catch (IOException ex) {
                }
            }
        });*/
        //Close BT button
        closeButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    closeBT();
//                    openButton.setEnabled(true);
 //                   closeButton.setEnabled(false);

                }
                catch (IOException ex) {

                }
            }
        });
        //Send Button
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    sendData();
                }
                catch (IOException ex) { }
            }
        });
    }
    void sendData() throws IOException
    {///////////////////////////////////////////////////////
        OutputStreamWriter writer = new OutputStreamWriter(mmSocket.getOutputStream());
        writer.write("Hello World!!!!!!!!!\r\n");
        writer.flush();
        ///////////////////////////////////////////////////

        /*
        putString msg = myTextbox.getText().toString();
        msg = msg.trim();
        msg += "\n";
        mmOutStream.write(msg.getBytes());*/

        //info_textview.setText("Data Sent");
    }
    // 페어링된 기기중에서 사용자가 선택한 블루투스를 실제로 찾는다.
    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(devicename))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        //speakOut("Bluetooth Device Found");
    }

    //찾은 블루투스를 안드로이드 폰과 연결한다
    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();


        is_connected = true;



        String msg = "start";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());

        //msg = "blinkOn13";
        //msg += "\n";
        //mmOutputStream.write(msg.getBytes());
    }


    //블루투스를 안드로이드와 연결 해제한다.
    void closeBT() throws IOException
    {
        String msg = "stop";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        try {

            Thread.sleep(100);

        } catch (InterruptedException e) { }


        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();

        is_connected = false;
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {

                    if(!is_connected)
                    {
                        continue;
                    }


                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "EUC-KR");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {

                                            info_textview.setText(data);

                                            String tmp2 = (String)data;


                                            String tmp = (String)info_textview.getText();

                                            info_textview.setText(tmp2);

                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }

                    try {

                        Thread.sleep(100);

                    } catch (InterruptedException e) { }
                }
            }
        });

        workerThread.start();
    }

}
