package com.tubeproject.algorithm;

import com.tubeproject.controller.StationWLine;

import java.util.List;

public class PathResponse implements Comparable {
    private List<StationWLine> quickest;
    private List<StationWLine> lessConnection;
    private double quickestWeight;
    private double lessConnectionWeight;

    public PathResponse() {

    }

    public PathResponse(List<StationWLine> quickest, double quickestWeight, List<StationWLine> lessConnection, double lessConnectionWeight) {
        this.quickest = quickest;
        this.quickestWeight = quickestWeight;
        this.lessConnection = lessConnection;
        this.lessConnectionWeight = lessConnectionWeight;
    }

    public List<StationWLine> getQuickest() {
        return quickest;
    }

    public void setQuickest(List<StationWLine> quickest) {
        this.quickest = quickest;
    }

    public List<StationWLine> getLessConnection() {
        return lessConnection;
    }

    public void setLessConnection(List<StationWLine> lessConnection) {
        this.lessConnection = lessConnection;
    }

    public double getQuickestWeight() {
        return quickestWeight;
    }

    public void setQuickestWeight(double quickestWeight) {
        this.quickestWeight = quickestWeight;
    }

    public double getLessConnectionWeight() {
        return lessConnectionWeight;
    }

    public void setLessConnectionWeight(double lessConnectionWeight) {
        this.lessConnectionWeight = lessConnectionWeight;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
