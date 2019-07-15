package com.tubeproject.model.builder;

import com.tubeproject.controller.ConnectionWLine;
import com.tubeproject.controller.StationWLine;

public class ConnectionWLineBuilder {
    private StationWLine station;
    private double duration;

    public ConnectionWLineBuilder setStation(StationWLine station) {
        this.station = station;
        return this;
    }

    public ConnectionWLineBuilder setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public ConnectionWLine createConnectionWLine() {
        return new ConnectionWLine(station, duration);
    }
}