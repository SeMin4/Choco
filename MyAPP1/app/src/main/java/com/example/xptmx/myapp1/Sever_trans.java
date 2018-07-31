package com.example.xptmx.myapp1;

public class Sever_trans {
    String device_id;
    String username;
    double userlatitude;
    double userlongitude;
    static Sever_trans instance = null;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getuserlatitude() {
        return userlatitude;
    }

    public void setUserlatitude(double userlatitude) {
        this.userlatitude = userlatitude;
    }

    public double getUserlongitude() {
        return userlongitude;
    }

    public void setUserlongitude(double userlongitude) {
        this.userlongitude = userlongitude;
    }
    synchronized public static Sever_trans getInstance()
    {
        if(instance == null)
        {
            instance = new Sever_trans();
        }
        return instance;
    }
}
