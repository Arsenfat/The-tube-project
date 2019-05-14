package com.tubeproject.model;

import com.tubeproject.controller.*;

public class Select {

    private User user;
    private Line line;
    private Fare fare;
    private Zone zone;
    private Route route;
    private Station station;
    private FavoriteRoute favoriteRoute;

    public Select(User user, Line line, Fare fare, Zone zone, Route route, Station station, FavoriteRoute favoriteRoute) {
        this.user = user;
        this.line = line;
        this.fare = fare;
        this.zone = zone;
        this.route = route;
        this.station = station;
        this.favoriteRoute = favoriteRoute;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public FavoriteRoute getFavoriteRoute() {
        return favoriteRoute;
    }

    public void setFavoriteRoute(FavoriteRoute favoriteRoute) {
        this.favoriteRoute = favoriteRoute;
    }
}