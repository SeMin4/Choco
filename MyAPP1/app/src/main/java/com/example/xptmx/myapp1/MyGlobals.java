package com.example.xptmx.myapp1;


import android.os.StrictMode;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

public class MyGlobals {
        private int page;
        private static MyGlobals instance = null;
        ArrayList<list_item> list_itemArrayList;
         list_item recent_list_item;

    public list_item getRecent_list_item() {
        return recent_list_item;
    }

    public void setRecent_list_item(list_item recent_list_item) {
        this.recent_list_item = recent_list_item;
    }


    public int getPage() {
        return page;
    }

    public void listCreate()
    {
        list_itemArrayList=new ArrayList<list_item>();
    }

    public void listDestroy()
    {
        list_itemArrayList.clear();
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<list_item> getList_itemArrayList() {
        return list_itemArrayList;
    }

    public void setList_itemArrayList(ArrayList<list_item> list_itemArrayList) {
        this.list_itemArrayList = list_itemArrayList;
    }

    public static void setInstance(MyGlobals instance) {
        MyGlobals.instance = instance;
    }

    public void makeRecentData()
    {
        StrictMode.enableDefaults();

        boolean isFirst=true;
        boolean inCreate_date = false, inLocation_id = false, inLocation_name = false, inMd101_sn = false, inMsg = false;
        String create_date = null, location_id = null, location_name = null, md101_sn = null, msg = null;

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

        recent_list_item=new list_item(create_date,msg,location_id,location_name,md101_sn);
    }

    public int makeCode(String s)
    {
        int code=7; //기본값

        if(s.contains("지진"))
            code=1;
        else if(s.contains("폭염"))
            code=2;
        else if(s.contains("낙뢰"))
            code=3;
        else if(s.contains("대설"))
            code=4;
        else if(s.contains("해일"))
            code=5;
        else if(s.contains("화재"))
            code=6;

        return code;
    }

    public void makeData(int page)
    {

        StrictMode.enableDefaults();
        boolean inCreate_date = false, inLocation_id = false, inLocation_name = false, inMd101_sn = false, inMsg = false;
        String create_date = null, location_id = null, location_name = null, md101_sn = null, msg = null;

        try {
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
                            if (parser.getName().equals("md101_sn")) { //md101 만나면 내용을 받을수 있게 하자
                                inMd101_sn = true;
                            }
                            if (parser.getName().equals("msg")) { //msg 만나면 내용을 받을수 있게 하자
                                inMsg = true;
                            }

                            if (parser.getName().equals("message")) { //message 태그를 만나면 에러 출력
                                //Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG);
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
        } catch (Exception e) {
        }
    }

        public static synchronized MyGlobals getInstance(){
                if(null == instance){
                    instance = new MyGlobals();
                }
            return instance;
        }
    }