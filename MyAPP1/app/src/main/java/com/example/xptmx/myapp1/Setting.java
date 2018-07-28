package com.example.xptmx.myapp1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;


public class Setting extends AppCompatActivity {


    static Handler myHandler;
    list_item my_list_item;
    boolean isFirst;

    boolean inCreate_date = false, inLocation_id = false, inLocation_name = false, inMd101_sn = false, inMsg= false;
    String create_date = null, location_id = null, location_name= null, md101_sn = null, msg = null; //new variables

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.setting);

        Switch aSwitch=(Switch)findViewById(R.id.alarmSwitch);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true)
                {
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    startService(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    stopService(intent);
                }
            }
        });

        Button btn=(Button)findViewById(R.id.simulation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyGlobals.getInstance().makeRecentData();
                my_list_item=MyGlobals.getInstance().getRecent_list_item();

            Message msg=myHandler.obtainMessage();
            msg.obj=my_list_item;
            myHandler.sendMessage(msg);//쓰레드에 있는 핸들러에게 메세지를 보냄
            int code=MyGlobals.getInstance().makeCode(my_list_item.getContent());
            }
        });
    }

     protected static void setHandler(Handler handler){
        myHandler=handler;
    }
}
