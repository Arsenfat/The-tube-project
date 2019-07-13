package com.tubeproject.controller;

import java.util.Objects;

public class StationWLine extends Station {
    private Line line;

    public StationWLine() {
    }

    public StationWLine(String naptan, String name, boolean wheelchair, double latitude, double longitude, Line line) {
        super(naptan, name, wheelchair, latitude, longitude);
        this.line = line;
    }

    public StationWLine(Station station, Line line) {
        this(station.getNaptan(), station.getName(), station.isWheelchair(), station.getLatitude(), station.getLongitude(), line);

    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StationWLine that = (StationWLine) o;
        return Objects.equals(line, that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), line);
    }

    @Override
    public String toString() {
        String capitalizedLine = getLine().getName().substring(0, 1).toUpperCase() + getLine().getName().substring(1).toLowerCase();
        return String.format("%s, %s", getName(), capitalizedLine);
    }
}
