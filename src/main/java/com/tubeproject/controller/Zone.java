package com.tubeproject.controller;

public class Zone {

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public Zone(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
