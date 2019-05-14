package com.tubeproject.controller;

import org.jgrapht.graph.AsWeightedGraph;

public class TubeGraph {

    private AsWeightedGraph graph;

    public TubeGraph(AsWeightedGraph graph) {
        this.graph = graph;
    }

    public AsWeightedGraph makeGraph() {
        return graph;
    }

    public AsWeightedGraph getGraph() {
        return graph;
    }

    public void setGraph(AsWeightedGraph graph) {
        this.graph = graph;
    }
}
