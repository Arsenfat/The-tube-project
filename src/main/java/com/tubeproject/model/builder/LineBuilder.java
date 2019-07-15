package com.tubeproject.model.builder;

import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;

import java.util.List;

public class LineBuilder {
    private int id;
    private String name;
    private List<Station> stations;

    public LineBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public LineBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public LineBuilder setStations(List<Station> stations) {
        this.stations = stations;
        return this;
    }

    public Line createLine() {
        return new Line(id, name, stations);
    }
}