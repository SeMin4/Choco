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
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {
    Intent customintent ;
    NotificationManager notificationManager;
    public int count = 0;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("users");
    String remoteuser;
    String getid;
    public static String langitude;
    public static String longitude;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        //remoteMessage
        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        remoteuser = remoteMessage.getNotification().getTag();
        int idx = remoteuser.indexOf("&");
        langitude = remoteuser.substring(0,idx);
        longitude = remoteuser.substring(idx+1);
        //Map<String, String> pushDataMap = remoteMessage.getData();
        //sendNotification(pushDataMap);
    }
    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, Warningpage2.class);
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
                .setContentText("주변에 상황 발생")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setTicker("알림!!")
                .setContentIntent(pendingIntent);
        notificationManager.notify(777, notificationBuilder.build());
    }


}
