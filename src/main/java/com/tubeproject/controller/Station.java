package com.tubeproject.controller;

public class Station {

    private String name;
    private boolean wheelchair;
    private Zone zone;

    public Station(String name, boolean wheelchair, Zone zone) {
        this.name = name;
        this.wheelchair = wheelchair;
        this.zone = zone;
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

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
}
