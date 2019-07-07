package com.tubeproject.tool;

import com.tubeproject.controller.Station;

public class StationMapPos extends Station {
    private double X;
    private double Y;

    public StationMapPos() {
        super();
    }

    public StationMapPos(Station station) {
        super(station.getNaptan(), station.getName(), false, station.getLatitude(), station.getLongitude());
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    @Override
    public String toString() {
        return String.format("(%.00f;%.00f) - %s", getX(), getY(), getName());
    }
}
