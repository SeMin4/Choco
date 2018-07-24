package com.example.xptmx.myapp1;

public class EarthquakeShelter {
    private String name;
    private double latitude;
    private double longitude;

    public EarthquakeShelter(String name, double lat, double lng){
        this.name = name;
        this.latitude = lat;
        this.longitude = lng;
    }

    @Override
    public String toString() {
        return String.format("[EQS] %s (%.2f, %.2f)", this.name, this.latitude, this.longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}
