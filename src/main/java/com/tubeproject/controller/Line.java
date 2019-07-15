package com.tubeproject.controller;

import java.util.List;
import java.util.Objects;

public class Line {

    private int id;
    private String name;
    private List<Station> stations;

    public Line() {

    }

    public Line(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Line(int id, String name, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return id == line.id &&
                name.equals(line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
