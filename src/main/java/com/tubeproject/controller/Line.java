package com.tubeproject.controller;

import java.util.ArrayList;

public class Line {

    private String name;
    private ArrayList<Station> stations = new ArrayList<Station>();

    public Line(String name, ArrayList<Station> stations) {
        this.name = name;
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
}
