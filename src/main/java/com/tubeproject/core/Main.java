package com.tubeproject.core;

import com.tubeproject.controller.*;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Select;
import com.tubeproject.model.requests.GetAllLinesWithStationsRequest;
import com.tubeproject.model.requests.LoginRequest;
import com.tubeproject.view.ViewMainScreen;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        //Toujours initialiser la context map
        Map<String, Object> ctxMap = ContextMap.getContextMap();
        ViewMainScreen.startWindow();

        Astar aStar = new Astar();


        //initialize the graph base on the example graph
        Node n0 = new Node("Station 0", 0, 1);
        Node n1 = new Node("Station 1", 1, 2);
        Node n2 = new Node("Station 2", 1, 0);
        Node n3 = new Node("Station 3", 2, 2);
        Node n4 = new Node("Station 4", 2, 0);
        Node n5 = new Node("Station 5", 3, 1);

        //initialize the edges

        //Station 0
        n0.setAdjacencies(new Edge[]{
                new Edge(n1,4),
                new Edge(n2,3),
                new Edge(n4,4)
        });

        //Station 1
        n1.setAdjacencies(new Edge[]{
                new Edge(n0,4),
                new Edge(n2,5),
                new Edge(n3,2),
                new Edge(n4,4)
        });


        //Station 2
        n2.setAdjacencies(new Edge[]{
                new Edge(n0,3),
                new Edge(n1,5),
                new Edge(n3,7)
        });

        //Station 3
        n3.setAdjacencies(new Edge[]{
                new Edge(n1,2),
                new Edge(n2,7),
                new Edge(n4,2)
        });


        //Station 4
        n4.setAdjacencies(new Edge[]{
                new Edge(n0,4),
                new Edge(n1,4),
                new Edge(n3,2),
                new Edge(n5,6)
        });

        //Station 5
        n5.setAdjacencies(new Edge[]{
                new Edge(n4,6),
        });

        aStar.AstarSearch(n1,n5);

        List<Node> path = aStar.printPath(n5);

        System.out.println("Path: " + path);


        try {
            DatabaseConnection.DatabaseOpen();

            GetAllLinesWithStationsRequest obj = new GetAllLinesWithStationsRequest();
            Select select = new Select(obj);
            Optional<?> opt = select.select();
            boolean connected;
            if (opt.isPresent()) {
                List<Line> l = new ArrayList<>();
                l = (List<Line>)opt.get();
                System.out.println("is ok: " + l.get(1).getStations());
                displayStationLocation(l);
            }
            else {
                System.out.println("Impossible to retrieve lines");
            }
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println("Error: Connection to the server failed.");
        }

    }

    public static void displayStationLocation (List<Line> l){

        for (Station value : l.get(1).getStations()){
            System.out.println("name is: " + value.getName() + " location: " + value.getLatitude() + " " + value.getLongitude());
        }

    }

}
