package com.tubeproject.model.builder;

import com.tubeproject.controller.Zone;

public class ZoneBuilder {
    private String name;
    private int id;

    public ZoneBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ZoneBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public Zone createZone() {
        return new Zone(name, id);
    }
}