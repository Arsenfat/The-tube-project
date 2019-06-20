package com.tubeproject.controller;

import org.w3c.dom.Node;

public class NodeListElement {

    private Vertice vertice;
    private double f;
    private double h;
    private double g;

    public NodeListElement(Vertice vertive, double f, double h, double g) {
        this.vertice = vertive;
        this.f = f;
        this.h = h;
        this.g = g;
    }

    public NodeListElement(){

    }

    public Vertice getVertice() {
        return vertice;
    }

    public void setVertice(Vertice vertice) {
        this.vertice = vertice;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void print() {
        System.out.println("f is: " + f + " g is: " + g + " h is: " +  h + " name is: " + this.vertice.getName());
    }
}
