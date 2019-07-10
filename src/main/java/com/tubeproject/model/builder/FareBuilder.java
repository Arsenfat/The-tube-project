package com.tubeproject.model.builder;

import com.tubeproject.controller.Fare;
import com.tubeproject.controller.Zone;

public class FareBuilder {
    private Zone departingZone;
    private Zone arrivingZone;
    private double price;
    private Fare.Type type;

    public FareBuilder setDepartingZone(Zone departingZone) {
        this.departingZone = departingZone;
        return this;
    }

    public FareBuilder setArrivingZone(Zone arrivingZone) {
        this.arrivingZone = arrivingZone;
        return this;
    }

    public FareBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public FareBuilder setType(Fare.Type type) {
        this.type = type;
        return this;
    }

    public Fare createFare() {
        return new Fare(departingZone, arrivingZone, price, type);
    }
}