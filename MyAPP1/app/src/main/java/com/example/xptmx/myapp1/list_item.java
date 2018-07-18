package com.example.xptmx.myapp1;

public class list_item {


    private String create_date;
    private String msg;
    private String location_id;
    private String location_name;
    private String md101_sn;

    public list_item(String create_date, String content, String location_id, String location_name, String md101_sn) {
        this.create_date = create_date;
        this.msg = content;
        this.location_id = location_id;
        this.location_name = location_name;
        this.md101_sn = md101_sn;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getContent() {
        return msg;
    }

    public void setContent(String content) {

        this.msg = content;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getMd101_sn() {
        return md101_sn;
    }

    public void setMd101_sn(String md101_sn) {
        this.md101_sn = md101_sn;
    }
}
