package com.example.xptmx.myapp1;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ArduinoSerivce extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        try {
            GlobalAduino.getInstance().findBT();
            GlobalAduino.getInstance().openBT();
            startForeground(1, new Notification());
        }catch (Exception e)
        {
            //
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행

    }
}