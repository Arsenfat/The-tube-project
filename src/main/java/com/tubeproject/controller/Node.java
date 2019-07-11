package com.tubeproject.controller;

import java.util.ArrayList;
import java.util.List;

public class Node{

    private final String value;
    private double g_scores;
    private double h_scores;
    private final double latitude;
    private final double longitude;
    private double f_scores = 0;
    private List<Edge> adjacencies = new ArrayList<Edge>();
    private Node parent;

    public Node(String val, double latitude, double longitude){
        this.value = val;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    //Calculate the distance from the goal node
    public double calculateH_cost(double goalLat, double goalLong){
        this.h_scores = Math.sqrt ( Math.pow((this.longitude - goalLat), 2) + Math.pow((this.latitude - goalLong), 2) );

        return this.h_scores;
    }

    public List<Edge> getAdjacencies() {
        return adjacencies;
    }

    public void setAdjacencies(List<Edge> adjacencies) {
        this.adjacencies = adjacencies;
    }

    public String getValue() {
        return value;
    }

    public double getG_scores() {
        return g_scores;
    }

    public void setG_scores(double g_scores) {
        this.g_scores = g_scores;
    }

    public double getH_scores(double goalLat, double goalLong) {
        this.calculateH_cost(goalLat, goalLong);
        return h_scores;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getF_scores() {
        return f_scores;
    }

    public void setF_scores(double f_scores) {
        this.f_scores = f_scores;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String toString(){
        return value;
    }

}