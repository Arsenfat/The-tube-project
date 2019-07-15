package com.tubeproject.algorithm;

import com.tubeproject.controller.StationWLine;

import java.util.List;

public class PathResponse implements Comparable {
    private List<StationWLine> quickest;
    private List<StationWLine> lessConnection;

    public PathResponse() {

    }

    public PathResponse(List<StationWLine> quickest, List<StationWLine> lessConnection) {
        this.quickest = quickest;
        this.lessConnection = lessConnection;
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

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
