package com.tubeproject.controller;

public class Fare {
    private Type type;

    private Zone departingZone;
    private Zone arrivingZone;
    private double price;

    public Fare(Zone departingZone, Zone arrivingZone, double price, Type type) {
        this.departingZone = departingZone;
        this.arrivingZone = arrivingZone;
        this.price = price;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public double calculation () {
        return this.price;
    }

    public Zone getDepartingZone() {
        return departingZone;
    }

    public void setDepartingZone(Zone departingZone) {
        this.departingZone = departingZone;
    }

    public Zone getArrivingZone() {
        return arrivingZone;
    }

    public void setArrivingZone(Zone arrivingZone) {
        this.arrivingZone = arrivingZone;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        CHILD,
        ADULT,
        OYSTER_PEAK,
        OYSTER_OFF_PEAK
    }
}
