package com.tubeproject.controller;

import java.util.Objects;

public class ConnectionWLine {
    private StationWLine station;
    private double duration;

    public ConnectionWLine(StationWLine station, double duration) {
        this.station = station;
        this.duration = duration;
    }

    public StationWLine getStation() {
        return station;
    }

    public void setStation(StationWLine station) {
        this.station = station;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionWLine that = (ConnectionWLine) o;
        return Double.compare(that.duration, duration) == 0 &&
                Objects.equals(station, that.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, duration);
    }

    @Override
    public String toString() {
        return "ConnectionWLine{" +
                "station=" + station +
                ", duration=" + duration +
                '}';
    }
}
