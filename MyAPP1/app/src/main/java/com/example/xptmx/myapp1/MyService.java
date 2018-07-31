package com.example.xptmx.myapp1;

import android.Manifest;
import android.app.Notification;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class MyService extends Service {
    MediaPlayer mp;
    NotificationManager Notifi_M;
    ServiceThread thread;
    list_item my_list_item;
    private TextToSpeech tts;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "서비스의 onCreate");
        mp = MediaPlayer.create(this, R.raw.mymusic);
        mp.setLooping(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("test", "서비스의 onStartCommand");
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        Setting.setHandler(handler);
        GlobalAduino.setHandler(handler);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        return START_STICKY;
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(MyService.this, WarningPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            my_list_item = (list_item) msg.obj;
            intent.putExtra("create_date", my_list_item.getCreate_date());
            intent.putExtra("location_id", my_list_item.getLocation_id());
            intent.putExtra("location_name", my_list_item.getLocation_name());
            intent.putExtra("md101_sn", my_list_item.getMd101_sn());
            intent.putExtra("msg", my_list_item.getContent());
            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder;

            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel mChannel = new NotificationChannel("myapp", "myapp", NotificationManager.IMPORTANCE_DEFAULT);
                Notifi_M.createNotificationChannel(mChannel);
                notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),mChannel.getId());
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
            } else
            {
                notificationBuilder=new NotificationCompat.Builder(getApplicationContext());
            }
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(my_list_item.getCreate_date())
                            .setContentText(my_list_item.getContent())
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .setTicker("알림!!")
                            .setContentIntent(pendingIntent);

            tts.speak(my_list_item.getContent(), TextToSpeech.QUEUE_ADD, null);
            //tts.speak("7월 22일 테스트 버전", TextToSpeech.QUEUE_ADD, null);
            //tts.speak(my_list_item.getContent(), TextToSpeech.QUEUE_ADD, null);

            Notifi_M.notify(777, notificationBuilder.build());
            int code=MyGlobals.getInstance().makeCode(my_list_item.getContent());
            try {
                GlobalAduino.getInstance().sendData(code);
            }catch(Exception e)
            {
                //
            }
            startActivity(intent);

            UtilFlash.flash_on();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.stopForever();
        thread = null;
        mp.stop();
        Log.d("test", "서비스의 onDestroy");

            if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
