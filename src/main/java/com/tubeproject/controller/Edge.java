package com.tubeproject.controller;

public class Edge{
    private final double cost;
    private final Node target;

    public Edge(Node targetNode, double costVal){
        this.target = targetNode;
        this.cost = costVal;
    }

    public double getCost() {
        return cost;
    }

    public Node getTarget() {
        return target;
    }

    public String toString(){
        return "Traget is: " + target.getValue() + " Cost id : " + cost;
    }
}