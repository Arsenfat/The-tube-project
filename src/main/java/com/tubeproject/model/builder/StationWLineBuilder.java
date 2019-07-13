package com.tubeproject.model.builder;

import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import com.tubeproject.controller.StationWLine;

public class StationWLineBuilder {
    private Line line;
    private Station station;

    public StationWLineBuilder setLine(Line line) {
        this.line = line;
        return this;
    }

    public StationWLineBuilder setStation(Station station) {
        this.station = station;
        return this;
    }

    public StationWLine createStationWLine() {
        return new StationWLine(station, line);
    }
}