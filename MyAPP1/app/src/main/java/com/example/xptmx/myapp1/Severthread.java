package com.example.xptmx.myapp1;


import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.Handler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Severthread extends Thread {
    URL url;
    HttpURLConnection urlConnection;
    String bh_name;
    double bh_longitude;
    double bh_latitude;
    public Severthread(double bh_longitude, double bh_latitude)
    {
        this.bh_name = "sex";
        this.bh_longitude = bh_longitude;
        this.bh_latitude = bh_latitude;
       // myHandler handler = new myHandler();
       // SecondLayout.setHandler(handler);
    }

   // class myHandler extends Handler {
   //     @Override
   //     public void handleMessage(android.os.Message msg) {
    //        String device_id = (String)msg.obj;
   //     }
  //  }
//
    @Override
    public void run() {
        while(true)
        {
            if(CustomFirebaseInstanceIdService.refreshedToken != null) try {
                url = new URL("http://172.30.7.144/gps_location.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                //if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    try {
                        String postData = "refresedToken="+CustomFirebaseInstanceIdService.refreshedToken+"&bh_name="+bh_name+"&bh_longitude="+bh_longitude+"&bh_latitude="+bh_latitude;
                        System.out.println("Refreshtoken"+CustomFirebaseInstanceIdService.refreshedToken);
                        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setConnectTimeout(7000);
                        urlConnection.setDoOutput(true);
                        urlConnection.setDoInput(true);
                        OutputStream outputStream = urlConnection.getOutputStream();
                        outputStream.write(postData.getBytes("UTF-8"));
                        outputStream.flush();
                        outputStream.close();
                        //String result = readStream(urlConnection.getInputStream());
                        //urlConnection.disconnect();
                        //return result;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        //Log.i("PHPRequest", "request was failed.");
                    }
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    readStream(in);
                    urlConnection.disconnect();
                //}else{

                //}
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void readStream(InputStream in){
        final String data = readData(in);
        newHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
    public String readData(InputStream is){
        String data = "";
        Scanner s = new Scanner(is);
        while(s.hasNext()) data += s.nextLine() + "\n";
        s.close();
        return data;
    }

    Handler newHandler = new Handler();
}

