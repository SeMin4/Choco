package com.example.xptmx.myapp1;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

public class ServiceThread extends Thread {
    Handler handler;
    boolean isRun=true;
    boolean isFirst;
    boolean dataExist=false;
    list_item my_list_item;

    boolean inCreate_date = false, inLocation_id = false, inLocation_name = false, inMd101_sn = false, inMsg= false;
    String create_date = null, location_id = null, location_name= null, md101_sn = null, msg = null; //new variables
    String recent_create_date = null, recent_location_id = null, recent_location_name= null, recent_md101_sn = null, recent_msg = null;

    public ServiceThread(Handler handler)
    {
        this.handler=handler;
    }

    public void stopForever(){
        synchronized (this){
            this.isRun=false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            StrictMode.enableDefaults();

            try{
                isFirst=true;
                URL url = new URL("http://data.mpss.go.kr/openapi/DisasterMsg"); //검색 URL부분

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(url.openStream(), null);

                int parserEvent = parser.getEventType();
                //System.out.println("파싱시작합니다.");

                while (isFirst){
                    switch(parserEvent){
                        case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                            if(parser.getName().equals("create_date")){ //title 만나면 내용을 받을수 있게 하자
                                inCreate_date = true;
                            }
                            if(parser.getName().equals("location_id")){ //address 만나면 내용을 받을수 있게 하자
                                inLocation_id = true;
                            }
                            if(parser.getName().equals("location_name")){ //mapx 만나면 내용을 받을수 있게 하자
                                inLocation_name = true;
                            }
                            if(parser.getName().equals("md101_sn")){ //mapy 만나면 내용을 받을수 있게 하자
                                inMd101_sn = true;
                            }
                            if(parser.getName().equals("msg")){ //mapy 만나면 내용을 받을수 있게 하자
                                inMsg = true;
                            }

                            if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                                Log.v("쓰레드 오류","쓰레드 오류");
                                //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                            }
                            break;

                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
                            if(inCreate_date){ //isTitle이 true일 때 태그의 내용을 저장.
                                create_date = parser.getText();
                                inCreate_date = false;
                            }
                            if(inLocation_id){ //isAddress이 true일 때 태그의 내용을 저장.
                                location_id = parser.getText();
                                inLocation_id = false;
                            }
                            if(inMd101_sn){ //isMapx이 true일 때 태그의 내용을 저장.
                                md101_sn = parser.getText();
                                inMd101_sn = false;
                            }
                            if(inMsg){ //isMapy이 true일 때 태그의 내용을 저장.
                                msg = parser.getText();
                                inMsg = false;
                            }
                            if(inLocation_name){ //isMapy이 true일 때 태그의 내용을 저장.
                                location_name = parser.getText();
                                inLocation_name = false;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("row")){
                                isFirst=false;
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch(Exception e){
                Log.v("쓰레드 오류","쓰레드 오류");
            }

            /*if(dataExist==false) {
                recent_create_date = create_date;
                recent_location_id = location_id;
                recent_location_name= location_name;
                recent_md101_sn = md101_sn;
                recent_msg = msg;
                dataExist=true;
            }
            else if(!md101_sn.equals(recent_md101_sn))
            {
                recent_create_date = create_date;
                recent_location_id = location_id;
                recent_location_name= location_name;
                recent_md101_sn = md101_sn;
                recent_msg = msg;

                my_list_item=new list_item(create_date,msg,location_id,location_name,md101_sn);
                Message msg=handler.obtainMessage();
                msg.obj=my_list_item;
                handler.sendMessage(msg);//쓰레드에 있는 핸들러에게 메세지를 보냄
            }*/

            my_list_item=new list_item(create_date,msg,location_id,location_name,md101_sn);
            Message msg=handler.obtainMessage();
            msg.obj=my_list_item;
            handler.sendMessage(msg);//쓰레드에 있는 핸들러에게 메세지를 보냄

            try{
                Thread.sleep(60000); //10초씩 쉰다.
            }catch (Exception e) {}
        }
    }
}