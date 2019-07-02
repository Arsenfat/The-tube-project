package com.tubeproject.controller;

public class Station {
    private String naptan;
    private String name;
    private boolean wheelchair;
    private double latitude;
    private double longitude;

    public Station() {
    }

    public Station(String naptan, String name, boolean wheelchair, double latitude, double longitude) {
        this.naptan = naptan;
        this.name = name;
        this.wheelchair = wheelchair;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getNaptan() {
        return naptan;
    }

    public void setNaptan(String naptan) {
        this.naptan = naptan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(boolean wheelchair) {
        this.wheelchair = wheelchair;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Station{" +
                "naptan='" + naptan + '\'' +
                ", name='" + name + '\'' +
                ", wheelchair=" + wheelchair +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
