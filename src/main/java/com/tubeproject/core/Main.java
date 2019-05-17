package com.tubeproject.core;

import com.tubeproject.controller.TubeGraph;

public class Main {
    public static void main(String[] args){
        System.out.println("Salut Sophie");

        int vertices = 6;
        TubeGraph graph = new TubeGraph(vertices);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 3, 2);
        graph.addEdge(1, 2, 5);
        graph.addEdge(2, 3, 7);
        graph.addEdge(3, 4, 2);
        graph.addEdge(4, 0, 4);
        graph.addEdge(4, 1, 4);
        graph.addEdge(4, 5, 6);
        graph.printGraph();

    }

}
