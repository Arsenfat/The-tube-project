package com.tubeproject.controller;

import java.util.Objects;

public class Station {
    protected String naptan;
    protected String name;
    protected boolean wheelchair;
    protected double latitude;
    protected double longitude;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return wheelchair == station.wheelchair &&
                Double.compare(station.latitude, latitude) == 0 &&
                Double.compare(station.longitude, longitude) == 0 &&
                naptan.equals(station.naptan) &&
                name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naptan, name, wheelchair, latitude, longitude);
    }
}
