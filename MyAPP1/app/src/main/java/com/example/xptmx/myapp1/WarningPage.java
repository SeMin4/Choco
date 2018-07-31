package com.example.xptmx.myapp1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorCompletionService;

import static android.speech.tts.TextToSpeech.ERROR;

public class WarningPage extends AppCompatActivity implements SensorEventListener {
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

    Boolean check = false;

    private static final String TAG = "AccelerometerActivity";

    private SensorManager sensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD =3000;

    private ConstraintLayout constraintLayout;

    private TextToSpeech tts;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_page);
        mGpsInfo = new GpsInfo(WarningPage.this);
        info.fcmToken = CustomFirebaseInstanceIdService.refreshedToken;
        info.latitude = mGpsInfo.getLatitude();
        info.longitude = mGpsInfo.getLongitude();
        constraintLayout = (ConstraintLayout) findViewById(R.id.cl_sensor);

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Intent intent = getIntent();
        final String msg = intent.getStringExtra("msg");

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        ImageView rabbit = (ImageView) findViewById(R.id.gif_image);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(rabbit);
        Glide.with(this).load(R.drawable.warning6).into(gifImage);


        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);


        final ImageButton lightbutton = (ImageButton) findViewById(R.id.lightbutton);
        lightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1 - i;
                if (i == 1) {
                    lightbutton.setImageResource(R.drawable.light_offbutton);
                    UtilFlash.flash_on();
                } else {
                    lightbutton.setImageResource(R.drawable.light_onbutton);
                    UtilFlash.flash_off();
                }

            }
        });

        //
        ImageButton homegobutton = (ImageButton) findViewById(R.id.homegobutton);
        homegobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        ImageButton shelfindbutton = (ImageButton) findViewById(R.id.shelfindbutton);
        shelfindbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Shelter.class);
                startActivity(intent);
            }
        });


        TextView tv = (TextView) findViewById(R.id.textView_warning);
        tv.setText(intent.getStringExtra("msg") + "\n" + intent.getStringExtra("create_date"));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        try {
            GlobalAduino.getInstance().sendData(0);
        }catch (Exception e){}
        UtilFlash.flash_off();
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
                        AlertDialog.Builder sos = new AlertDialog.Builder(
                                WarningPage.this);

                        sos.setTitle("도움 요청하기");
                        sos.setMessage("도움 요청이 되었습니다.");

                        sos.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                sendPostToFCM(info);
                                UtilFlash.flash_off();
                                dialog.dismiss();

                            }
                        });
                        check = true;
                        sos.show();
                    } else if (speed < 10) {

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