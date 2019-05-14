package com.tubeproject.controller;

import java.util.ArrayList;

public class Algorithm {

    private TubeGraph graph;
    private ArrayList<Route> routes;

    public Algorithm(TubeGraph graph) {
        this.graph = graph;
    }

    public ArrayList<Route> calculation() {
        return routes;
    }

    public TubeGraph getGraph() {
        return graph;
    }

    public void setGraph(TubeGraph graph) {
        this.graph = graph;
    }
}
