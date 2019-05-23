package com.tubeproject.core;

import com.tubeproject.controller.TubeGraph;

public class Main {
    public static void main(String[] args){
        int vertices = 6;
        TubeGraph graph = new TubeGraph(vertices);
        graph.addEdge(0, 1, 4, "Station 0", 36.385913, -127.441406);
        graph.addEdge(0, 2, 3, "Station 0", 36.385913, -127.441406);
        graph.addEdge(1, 3, 2, "Station 1", 45.374413, -122.434457);
        graph.addEdge(1, 2, 5, "Station 1", 45.374413, -122.434457);
        graph.addEdge(2, 3, 7, "Station 2", 26.097899, -119.456467);
        graph.addEdge(3, 4, 2, "Station 3", 27.234467, -103.434325);
        graph.addEdge(4, 0, 4, "Station 4", 25.324324, -112.434353);
        graph.addEdge(4, 1, 4, "Station 4", 25.324324, -112.434353);
        graph.addEdge(4, 5, 6, "Station 4", 25.324324, -112.434353);
        graph.printGraph();

    }

}
