package com.tubeproject.controller;

import java.util.Objects;

public class Zone implements Comparable<Zone> {

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public Zone(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zone zone = (Zone) o;
        return id == zone.id &&
                name.equals(zone.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Zone z) {
        return Integer.compare(this.getId(), z.getId());
    }
}
