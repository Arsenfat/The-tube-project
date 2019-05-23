package com.tubeproject.controller;

import java.util.LinkedList;

public class TubeGraph {

    private int vertices;
    private LinkedList<Edge>[] adjacencylist;

    public TubeGraph(int vertices) {
        this.vertices = vertices;
        adjacencylist = new LinkedList[vertices];
        //initialize adjacency lists for all the vertices
        for (int i = 0; i <vertices ; i++) {
            adjacencylist[i] = new LinkedList<>();
        }
    }

    public void addEdge(int source, int destination, int weight, String name, double latitude, double longitude) {
        Edge edge = new Edge(source, destination, weight, name, latitude, longitude);
        adjacencylist[source].addFirst(edge); //for directed graph
    }

    public void printGraph(){
        for (int i = 0; i <vertices ; i++) {
            LinkedList<Edge> list = adjacencylist[i];
            for (int j = 0; j <list.size() ; j++) {
                System.out.println("vertex-" + i + " is connected to " +
                        list.get(j).getDestination() + " with weight " +  list.get(j).getWeight() + " with the name " + list.get(j).getName() + " and the latitude " + list.get(j).getLatitude() + " and longitude " + list.get(j).getLongitude());
            }
        }
    }
}
