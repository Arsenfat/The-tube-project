package com.tubeproject.tool;

import com.tubeproject.controller.Line;

import java.util.List;

public class LineMap extends Line {
    private List<StationMapPos> stationTool;

    public LineMap() {
        super();
    }

    public LineMap(Line line, List<StationMapPos> stations) {
        super(line.getId(), line.getName());
        this.stationTool = stations;
    }


    public List<StationMapPos> getStationTool() {
        return stationTool;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", getId(), getName());
    }


}
