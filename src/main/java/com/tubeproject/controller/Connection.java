package com.tubeproject.controller;

import java.util.Objects;

public class Connection {
    private Station station;
    private double duration;

    public Connection(Station station, double duration) {
        this.station = station;
        this.duration = duration;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "station=" + station +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Double.compare(that.duration, duration) == 0 &&
                station.equals(that.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, duration);
    }
}
