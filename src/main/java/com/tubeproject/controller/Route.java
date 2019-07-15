package com.tubeproject.controller;

public class Route {

    private Station startingStation;
    private Station arrivingStation;

    public Route(Station startingStation, Station arrivingStation) {
        this.startingStation = startingStation;
        this.arrivingStation = arrivingStation;
    }

    public Station getStartingStation() {
        return startingStation;
    }

    public void setStartingStation(Station startingStation) {
        this.startingStation = startingStation;
    }

    public Station getArrivingStation() {
        return arrivingStation;
    }

    public void setArrivingStation(Station arrivingStation) {
        this.arrivingStation = arrivingStation;
    }
}
