package com.tubeproject.controller;

public class Fare {

    private Zone departingZone;
    private Zone arrivingZone;
    private double price;


    public Fare(Zone departingZone, Zone arrivingZone, double price) {
        this.departingZone = departingZone;
        this.arrivingZone = arrivingZone;
        this.price = price;
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
}
