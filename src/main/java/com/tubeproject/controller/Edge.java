package com.tubeproject.controller;

public class Edge {
    private int source;
    private int destination;
    private int weight;
    private String name;
    private double latitude;
    private double longitude;

    public Edge(int source, int destination, int weight, String name, double latitude, double longitude) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString(){
        return ("Source is: " + this.source + " destination is: " + this.destination + " weight is: " + this.weight + " name is: " + this.name);
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
}
