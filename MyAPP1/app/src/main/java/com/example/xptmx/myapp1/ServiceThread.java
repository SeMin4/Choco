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

            MyGlobals.getInstance().makeRecentData();
            my_list_item=MyGlobals.getInstance().getRecent_list_item();
            create_date=my_list_item.getCreate_date();
            location_id=my_list_item.getLocation_id();
            location_name=my_list_item.getLocation_name();
            md101_sn=my_list_item.getMd101_sn();
            msg=my_list_item.getContent();

            if(dataExist==false) {
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
            }

            /*my_list_item=new list_item(create_date,msg,location_id,location_name,md101_sn);
            Message msg=handler.obtainMessage();
            msg.obj=my_list_item;
            handler.sendMessage(msg);//쓰레드에 있는 핸들러에게 메세지를 보냄*/

            try{
                Thread.sleep(60000); //60초씩 쉰다.
            }catch (Exception e) {}
        }
    }
}