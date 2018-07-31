package com.example.xptmx.myapp1;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.content.Intent;


import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SecondLayout extends AppCompatActivity{
    GpsInfo mGpsInfo;
    static Handler handler;
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String SERVER_KEY = "AAAA1nQL7RE:APA91bHPZjMK5B0tm5lGg2Xqnon1x0NBbeUjAUQamCe45vZxoDrzxvIJdAUuRC0hmnKDT0Tx5swLYGMZOnOVESL93Q60DdalxH_-B1E8j5lIR2-C_INoEyVG0eq3LjTiXesMULJhlVQ0I3zsuUXwxxKXVlQJYhEpoA";
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
    public void setMyMessagingService(CustomFirebaseMessagingService messagingService) {
        this.myMessagingService = myMessagingService;
    }

    public CustomFirebaseMessagingService getMyMessagingService() {
        return myMessagingService;
    }

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.second_layout);
        mGpsInfo = new GpsInfo(SecondLayout.this);
        info.fcmToken = CustomFirebaseInstanceIdService.refreshedToken;
        info.latitude = mGpsInfo.getLatitude();
        info.longitude = mGpsInfo.getLongitude();
        ImageButton b1 = (ImageButton) findViewById(R.id.report1);
        ImageButton b2 = (ImageButton) findViewById(R.id.report2);
        ImageButton b3 = (ImageButton) findViewById(R.id.report3);
        ImageButton b4 = (ImageButton) findViewById(R.id.report4);
        ImageButton b5 = (ImageButton) findViewById(R.id.report5);
        ImageButton b6 = (ImageButton) findViewById(R.id.report6);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(SecondLayout.this);
                ad.setTitle("화재 제보하기");       // 제목 설정
                ad.setMessage("제보하시겠습니까?");   // 내용 설정
                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String devicedid = Sever_trans.getInstance().getDevice_id();
                        sendPostToFCM(info);                    ;


                        dialog.dismiss();
                        // Event
                    }
                });
                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                      //  Log.v(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 창 띄우기
                ad.show();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(SecondLayout.this);

                ad.setTitle("지진");       // 제목 설정
                ad.setMessage("제보하시겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPostToFCM(info);

                        //  Log.v(TAG,"Yes Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Log.v(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 창 띄우기
                ad.show();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(SecondLayout.this);

                ad.setTitle("폭염");       // 제목 설정
                ad.setMessage("제보하시겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPostToFCM(info);
                        //  Log.v(TAG,"Yes Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Log.v(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 창 띄우기
                ad.show();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(SecondLayout.this);

                ad.setTitle("낙뢰");       // 제목 설정
                ad.setMessage("제보하시겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPostToFCM(info);
                        //  Log.v(TAG,"Yes Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Log.v(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 창 띄우기
                ad.show();
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(SecondLayout.this);

                ad.setTitle("대설");       // 제목 설정
                ad.setMessage("제보하시겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPostToFCM(info);
                        //  Log.v(TAG,"Yes Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Log.v(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 창 띄우기
                ad.show();
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(SecondLayout.this);

                ad.setTitle("해일");       // 제목 설정
                ad.setMessage("제보하시겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPostToFCM(info);
                        //  Log.v(TAG,"Yes Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Log.v(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                // 창 띄우기
                ad.show();
            }
        });
        BottomNavigationView bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent0=new Intent(SecondLayout.this,MainActivity.class);
                        startActivity(intent0);
                        return true;
                    case R.id.navigation_shelter:
                        Intent intent1=new Intent(SecondLayout.this,Shelter.class);
                        startActivity(intent1);
                        return true;
                    case R.id.navigation_message:
                        Intent intent2=new Intent(SecondLayout.this,Disater_message.class);
                        startActivity(intent2);
                        return true;
                    case R.id.navigation_tips:
                        Intent intent3=new Intent(SecondLayout.this,Tips.class);
                        startActivity(intent3);
                        return true;
                    case R.id.navigation_setting:
                        Intent intent4=new Intent(SecondLayout.this,Setting.class);
                        startActivity(intent4);
                        return true;
                }
                return false;
            }
        });

        Menu menu=bottomNavigation.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
    }
    public static void  setHandler(Handler handler2)
    {
        handler = handler2;
    }
    public void transposeSever(String device_id){
        URL url;
        HttpURLConnection urlConnection;
        if(CustomFirebaseInstanceIdService.refreshedToken != null){
            try {

                url = new URL("http://172.30.7.144/Report.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                //if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                try {
                    String postdata = "refreshedToken=" + device_id;
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    OutputStream outputStream = urlConnection.getOutputStream();
                    outputStream.write(postdata.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                    //String result = readStream(urlConnection.getInputStream());
                    //urlConnection.disconnect();
                    //return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.i("PHPRequest", "request was failed.");
                }
                // InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //readStream(in);
                urlConnection.disconnect();
                //}else{

                //}


            }
            catch (Exception e)
            {

            }
        }
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

                                URL Url = new URL(FCM_MESSAGE_URL);
                                HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setDoOutput(true);
                                conn.setDoInput(true);
                                conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
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



