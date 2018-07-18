package com.example.xptmx.myapp1;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

public class Disater_message extends AppCompatActivity{

    ListView listView;
    MyListAdapter myListAdapter;
    ArrayList<list_item> list_itemArrayList;
    int page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disater_message);

        listView = findViewById(R.id.my_listview);
        list_itemArrayList = new ArrayList<list_item>();


        StrictMode.enableDefaults();
        boolean inCreate_date = false, inLocation_id = false, inLocation_name = false, inMd101_sn = false, inMsg = false;
        String create_date = null, location_id = null, location_name = null, md101_sn = null, msg = null;

        try {
            while (page <= 5) {
                String temp_url = "http://data.mpss.go.kr/openapi/DisasterMsg?pIndex=" + page;
                URL url = new URL(temp_url); //검색 URL부분

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(url.openStream(), null);

                int parserEvent = parser.getEventType();
                //System.out.println("파싱시작합니다.");

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                            if (parser.getName().equals("create_date")) { //date 만나면 내용을 받을수 있게 하자
                                inCreate_date = true;
                            }
                            if (parser.getName().equals("location_id")) { //id 만나면 내용을 받을수 있게 하자
                                inLocation_id = true;
                            }
                            if (parser.getName().equals("location_name")) { //name 만나면 내용을 받을수 있게 하자
                                inLocation_name = true;
                            }
                            if (parser.getName().equals("md101_sn")) { //mapy 만나면 내용을 받을수 있게 하자
                                inMd101_sn = true;
                            }
                            if (parser.getName().equals("msg")) { //msg 만나면 내용을 받을수 있게 하자
                                inMsg = true;
                            }

                            if (parser.getName().equals("message")) { //message 태그를 만나면 에러 출력
                                Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG);
                            }
                            break;

                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
                            if (inCreate_date) { //isTitle이 true일 때 태그의 내용을 저장.
                                create_date = parser.getText();
                                inCreate_date = false;
                            }
                            if (inLocation_id) { //isAddress이 true일 때 태그의 내용을 저장.
                                location_id = parser.getText();
                                inLocation_id = false;
                            }
                            if (inMd101_sn) { //isMapx이 true일 때 태그의 내용을 저장.
                                md101_sn = parser.getText();
                                inMd101_sn = false;
                            }
                            if (inMsg) { //isMapy이 true일 때 태그의 내용을 저장.
                                msg = parser.getText();
                                inMsg = false;
                            }
                            if (inLocation_name) { //isMapy이 true일 때 태그의 내용을 저장.
                                location_name = parser.getText();
                                inLocation_name = false;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("row")) {
                                list_itemArrayList.add(
                                        new list_item(create_date, msg, location_id, location_name, md101_sn)
                                );
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
                page += 1;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG);
        }
        myListAdapter = new MyListAdapter(Disater_message.this, list_itemArrayList);
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),Disaster_message_onClicked.class);
                intent.putExtra("create_date",list_itemArrayList.get(position).getCreate_date());
                intent.putExtra("location_id",list_itemArrayList.get(position).getLocation_id());
                intent.putExtra("location_name",list_itemArrayList.get(position).getLocation_name());
                intent.putExtra("md101_sn",list_itemArrayList.get(position).getMd101_sn());
                intent.putExtra("msg",list_itemArrayList.get(position).getContent());
                startActivity(intent);

            }
        });
    }

    public void onClick_2(View v)
    {
        finish();
    }

}
