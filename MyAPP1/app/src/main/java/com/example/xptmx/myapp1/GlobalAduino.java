package com.example.xptmx.myapp1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class GlobalAduino{
    private static GlobalAduino instance = null;

    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
    long mNow;
    Date mDate;

    static Handler handler2;
    list_item my_list_item;
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

    TextView info_textview;

    void sendData(int code) throws IOException
    {///////////////////////////////////////////////////////
        OutputStreamWriter writer = new OutputStreamWriter(mmSocket.getOutputStream());
        String msg = String.format("%d",code);
        mmOutputStream.write(msg.getBytes());
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    // 페어링된 기기중에서 사용자가 선택한 블루투스를 실제로 찾는다.
    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

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
        StrictMode.enableDefaults();
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

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
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
                                            if(data.equals("FIRE\r"))
                                            {
                                                my_list_item=new list_item(getTime(),"아두이노 시스템에서 화재가 감지되었습니다. 대피하여 주시기 바랍니다",
                                                        "null","null","null");

                                                Message msg=handler2.obtainMessage();
                                                msg.obj=my_list_item;
                                                handler2.sendMessage(msg);//쓰레드에 있는 핸들러에게 메세지를 보냄
                                            }

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

    public static void setHandler(Handler handler){
        handler2=handler;
    }


    public static synchronized GlobalAduino getInstance(){
        if(null == instance){
            instance = new GlobalAduino();
        }
        return instance;
    }
}
