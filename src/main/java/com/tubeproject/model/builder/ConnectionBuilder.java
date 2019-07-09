package com.tubeproject.model.builder;

import com.tubeproject.controller.Connection;
import com.tubeproject.controller.Station;

public class ConnectionBuilder {
    private Station station;
    private double duration;

    public ConnectionBuilder setStation(Station station) {
        this.station = station;
        return this;
    }

    public ConnectionBuilder setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public Connection createConnection() {
        return new Connection(station, duration);
    }
}