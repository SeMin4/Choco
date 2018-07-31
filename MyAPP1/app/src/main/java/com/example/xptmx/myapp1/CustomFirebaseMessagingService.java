package com.example.xptmx.myapp1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {
    NotificationManager notificationManager;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        //String To = remoteMessage.getData().get("getid");
      //  Toast.makeText(this, To, Toast.LENGTH_LONG).show();
        if(remoteMessage.getData().size() >0){
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }
        if(remoteMessage.getData().get("body")!= null){
            showNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("message"));
        }



        //Map<String, String> pushDataMap = remoteMessage.getData();
        //sendNotification(pushDataMap);
    }
    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, WarningPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(CustomFirebaseMessagingService.this, 1, intent,
                PendingIntent.FLAG_ONE_SHOT);
         notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel("myapp", "myapp", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),mChannel.getId());
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
        } else
        {
            notificationBuilder=new NotificationCompat.Builder(getApplicationContext());
        }
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Warning")
                .setContentText("주변에 화재 발생")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setTicker("알림!!")
                .setContentIntent(pendingIntent);
        notificationManager.notify(777, notificationBuilder.build());
    }


}
