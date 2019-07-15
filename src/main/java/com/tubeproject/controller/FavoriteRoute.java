package com.tubeproject.controller;

public class FavoriteRoute extends Route {

    private String name;

    public FavoriteRoute(Station startingStation, Station arrivingStation, String name) {
        super(startingStation, arrivingStation);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
