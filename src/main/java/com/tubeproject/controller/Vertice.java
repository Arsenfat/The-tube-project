package com.tubeproject.controller;

import java.util.ArrayList;

public class Vertice {

    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private ArrayList<Edge> edges = new ArrayList<Edge>();

    public Vertice(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public void addEdge(int source, int destination, int weight, String name, double latitude, double longitude) {
        Edge edge = new Edge(source, destination, weight, name, latitude, longitude);
        edges.add(edge);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }
}
