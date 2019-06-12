package com.tubeproject.controller;

import java.util.ArrayList;

public class TubeGraph {

    private ArrayList<Vertice> vertices = new ArrayList<Vertice>();

    public TubeGraph() {

    }

    public void addVertice(Vertice vertice){
        vertices.add(vertice);
        System.out.println("inside vertices " + vertices.get(0).getEdges().size());
    }

    public void printGraph(){
        System.out.println("inside printGraph " + vertices.get(0).getEdges().size());
        for (int i = 0; i < vertices.size() ; i++) {
            System.out.println("Name " + vertices.get(i).getName() + " edges: " + vertices.get(i).getEdges().size());
            for (int j = 0; j < vertices.get(i).getEdges().size(); j++)
            {
                System.out.println("Edges are: " + vertices.get(i).getEdges().get(j).toString());
            }
        }
    }

    public ArrayList<Vertice> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertice> vertices) {
        this.vertices = vertices;
    }
}


/**        TubeGraph graph = new TubeGraph();

 for (int i = 0; i < 10; i++)
 {
 Vertice vertice = new Vertice(2, "Station test2", 26.097899, -112.434353);
 vertice.addEdge(i, 1, 4, "Station A" + i, 36.385913, -127.441406);
 vertice.addEdge(i, 2, 3, "Station A" + i, 36.385913, -127.441406);
 vertice.addEdge(i, 3, 2, "Station A" + i, 45.374413, -122.434457);
 vertice.addEdge(i, 2, 5, "Station A" + i, 45.374413, -122.434457);
 vertice.addEdge(i, 3, 7, "Station A" + i, 26.097899, -119.456467);
 vertice.addEdge(i, 4, 2, "Station A" + i, 27.234467, -103.434325);
 vertice.addEdge(i, 0, 4, "Station A" + i, 25.324324, -112.434353);
 vertice.addEdge(i, 1, 4, "Station A" + i, 25.324324, -112.434353);
 vertice.addEdge(i, 5, 6, "Station A" + i, 25.324324, -112.434353);
 graph.addVertice(vertice);

 }

 graph.printGraph();

 System.out.println(graph.getVertices());

 DatabaseConnection dbconnection = new DatabaseConnection();

 dbconnection.DatabaseConnection();/