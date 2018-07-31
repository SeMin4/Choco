package com.example.xptmx.myapp1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.FragmentManager;
import android.app.Fragment;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nhn.android.maps.maplib.NGeoPoint;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Handler mHandler;
    private ProgressDialog mProgressDialog;
    private NGeoPoint mygeoPoint;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("users");
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CustomFirebaseInstanceIdService customFirebaseInstanceIdService;
    protected GpsInfo mGpsInfo;
    private Severthread mseverthread;
    private  ValueEventListener postListener;
    public userinfo info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.BLUETOOTH}, 1);
        mGpsInfo = new GpsInfo(MainActivity.this);
        customFirebaseInstanceIdService = new CustomFirebaseInstanceIdService();
        customFirebaseInstanceIdService.onTokenRefresh();
        info = new userinfo();
        info.fcmToken = CustomFirebaseInstanceIdService.refreshedToken;
        info.latitude = mGpsInfo.getLatitude();
        info.longitude = mGpsInfo.getLongitude();
        databaseReference.child("gps_info").setValue(info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton disaterButton = (ImageButton) findViewById(R.id.disaster_message);
        ImageButton shelterButton = (ImageButton) findViewById(R.id.shelter);
        ImageButton tipsButton = (ImageButton) findViewById(R.id.tips);
        ImageButton settingButton = (ImageButton) findViewById(R.id.setting);
        disaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler = new Handler();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog = ProgressDialog.show(MainActivity.this, "",
                                "잠시만 기다려 주세요.", true);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 3000);
                    }
                });
                Intent intent = new Intent(getApplicationContext(), Disater_message.class);
                startActivity(intent);
            }
        });
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setting.class);
                startActivity(intent);
            }
        });
        shelterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler = new Handler();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog = ProgressDialog.show(MainActivity.this, "",
                                "잠시만 기다려 주세요.", true);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 3000);
                    }
                });
                Intent intent = new Intent(getApplicationContext(), Shelter.class);
                startActivity(intent);

            }
        });
        tipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tips.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);
        Sever_trans.getInstance().setUserlongitude(mGpsInfo.getLongitude());
        Sever_trans.getInstance().setUserlatitude(mGpsInfo.getLatitude());
        mseverthread = new Severthread(mGpsInfo.getLongitude(),mGpsInfo.getLatitude());
        mseverthread.start();
        //HttpPostData();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager manager = getFragmentManager();

        if (id == R.id.nav_first_layout) {
            Intent intent = new Intent(getApplicationContext(), FirstLayout.class);
            startActivity(intent);
        } else if (id == R.id.nav_second_layout) {
            Intent intent = new Intent(getApplicationContext(), SecondLayout.class);

            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
