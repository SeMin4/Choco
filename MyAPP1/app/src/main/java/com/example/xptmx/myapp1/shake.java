package com.example.xptmx.myapp1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class shake extends AppCompatActivity implements SensorEventListener{
    GpsInfo mGpsInfo;
    CustomFirebaseMessagingService myMessagingService ;
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = firebaseDatabase.getReference("users");
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public userinfo info = new userinfo();
    public int count = 0;
    public String getid;
    public double secondlatitude;
    public CustomFirebaseMessagingService messagingService;
    public double secondlongitude;

    private static final String TAG = "AccelerometerActivity";

    private SensorManager sensorManager;
    private Sensor senAccelerometer;

    Boolean check = false;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD =3000;

    private ConstraintLayout constraintLayout;
    private TextView tvSensorStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        mGpsInfo = new GpsInfo(shake.this);
        info.fcmToken = CustomFirebaseInstanceIdService.refreshedToken;
        info.latitude = mGpsInfo.getLatitude();
        info.longitude = mGpsInfo.getLongitude();

        constraintLayout = (ConstraintLayout) findViewById(R.id.cl_sensor);
        tvSensorStatus = (TextView) findViewById(R.id.tv_status);

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        BottomNavigationView bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent0=new Intent(shake.this,MainActivity.class);
                        startActivity(intent0);
                        return true;
                    case R.id.navigation_shelter:
                        Intent intent1=new Intent(shake.this,Shelter.class);
                        startActivity(intent1);
                        return true;
                    case R.id.navigation_message:
                        Intent intent2=new Intent(shake.this,Disater_message.class);
                        startActivity(intent2);
                        return true;
                    case R.id.navigation_tips:
                        Intent intent3=new Intent(shake.this,Tips.class);
                        startActivity(intent3);
                        return true;
                    case R.id.navigation_setting:
                        Intent intent4=new Intent(shake.this,Setting.class);
                        startActivity(intent4);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis(); // 현재시간
            if((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if(check == false) {
                    if (speed > SHAKE_THRESHOLD) {
                        //지정된 수치이상 흔들림이 있으면 실행
                        constraintLayout.setBackgroundColor(Color.rgb(255, 0, 0));
                        tvSensorStatus.setText("흔들림 감지!");

                        AlertDialog.Builder sos = new AlertDialog.Builder(
                                shake.this);

                        sos.setTitle("도움 요청하기");
                        sos.setMessage("도움 요청이 되었습니다.");

                        sos.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                sendPostToFCM(info);
                                dialog.dismiss();
                            }
                        });
                        check = true;
                        sos.show();
                    } else if (speed < 10) {
                        constraintLayout.setBackgroundColor(Color.rgb(255, 255, 255));
                        tvSensorStatus.setText("흔들림 없음!");
                    }
                }
                //갱신
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void sendPostToFCM(final userinfo token) {
        databaseReference.child("gps_info")
                .addValueEventListener(infoListener);
    }
    ValueEventListener infoListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if (count % 3 == 0) {
                    getid = (String) snapshot.getValue();
                } else if (count % 3 == 1) {
                    secondlatitude = (double) snapshot.getValue();
                } else if (count % 3 == 2) {
                    secondlongitude = (double)snapshot.getValue();
                    if (distance_in_meter(info.getLatitude(), info.getLongitude(), secondlatitude, secondlongitude) < 2) {
                        try {
                            // FMC 메시지 생성 start
                            JSONObject root = new JSONObject();
                            JSONObject notification = new JSONObject();
                            notification.put("body", "warnig");
                            notification.put("title", getString(R.string.app_name));
                            notification.put("tag",secondlatitude+"&"+secondlongitude);
                            root.put("notification", notification);
                            root.put("to", getid);


                            // FMC 메시지 생성 end

                            URL Url = new URL(SecondLayout.FCM_MESSAGE_URL);
                            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.addRequestProperty("Authorization", "key=" + SecondLayout.SERVER_KEY);
                            conn.setRequestProperty("Accept", "application/json");
                            conn.setRequestProperty("Content-type", "application/json");
                            OutputStream os = conn.getOutputStream();
                            os.write(root.toString().getBytes("utf-8"));
                            os.flush();
                            conn.getResponseCode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                count++;






            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };




    public double distance_in_meter(double cur_latitude, double cur_longitude, double serv_latitude, double serv_longitude) {
        cur_latitude *=111;
        cur_longitude *= 111;
        serv_latitude *= 111;
        serv_longitude *= 111;
        double dLat = (cur_latitude - serv_latitude) ;
        double dLon = (cur_longitude - serv_longitude);
        double d = Math.sqrt(dLat*dLat + dLon*dLon);
        return d;
    }

}