package com.tubeproject.model.builder;

import com.tubeproject.controller.Station;

public class StationBuilder {
    private String naptan;
    private String name;
    private boolean wheelchair = false;
    private double latitude = 0;
    private double longitude = 0;

    public StationBuilder setNaptan(String naptan) {
        this.naptan = naptan;
        return this;
    }

    public StationBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public StationBuilder setWheelchair(boolean wheelchair) {
        this.wheelchair = wheelchair;
        return this;
    }

    public StationBuilder setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public StationBuilder setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Station createStation() {
        return new Station(naptan, name, wheelchair, latitude, longitude);
    }
}