package com.tubeproject.controller;

import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Select;
import com.tubeproject.model.requests.GetAllConnections;
import com.tubeproject.model.requests.GetAllLinesWithStationsRequest;
import com.tubeproject.model.requests.GetAllStationsRequest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GraphCreation {
    private List<Node> nodes = new ArrayList<Node>();
    private List<Edge> edges = new ArrayList<Edge>();
    private List<Station> stations = new ArrayList<Station>();


    public GraphCreation() {
    }


    public void getData() throws SQLException {
        //Toujours initialiser la context map
        Map<String, Object> ctxMap = ContextMap.getContextMap();

        DatabaseConnection.DatabaseOpen();
        GetAllConnections gac = new GetAllConnections();
        Select s = new Select(gac);
        Map<Station, List<Connection>> map = (Map<Station, List<Connection>>) s.select().get();
        System.out.println(map);

        /*nodes = map
                .keySet()
                .stream()
                .map(station -> new Node(station.getName(), station.getLatitude(), station.getLongitude()))
                .collect(Collectors.toList());*/

        for (Station station : map.keySet()){
            this.nodes.add(new Node(station.getName(), station.getLatitude(), station.getLongitude()));
            /*for (Connection connection : map.get(station))
            {
                edges.add(new Edge(n1,connection.getDuration()))
            }*/
        }

    }

    public void getStation() throws SQLException {

        DatabaseConnection.DatabaseOpen();
        GetAllStationsRequest gas = new GetAllStationsRequest();
        Select select = new Select(gas);
        Optional<?> opt = select.select();
        if (opt.isPresent()) {
            stations = (List<Station>)opt.get();
            System.out.println(stations.size());
        }
        else {
            System.out.println("Impossible to retrieve Station");
        }
    }

    public void getLine() throws SQLException {
            DatabaseConnection.DatabaseOpen();

            GetAllLinesWithStationsRequest obj = new GetAllLinesWithStationsRequest();
            Select select = new Select(obj);
            Optional<?> opt = select.select();
            if (opt.isPresent()) {
                List<Line> l = new ArrayList<>();
                l = (List<Line>)opt.get();
                this.displayStationLocation(l);
            }
            else {
                System.out.println("Impossible to retrieve lines");
            }
            DatabaseConnection.DatabaseClose();
            System.out.println("Error: Connection to the server failed.");
    }


    public void displayStationLocation (List<Line> l){

        for (Station value : l.get(1).getStations()){
            System.out.println("name is: " + value.getName() + " location: " + value.getLatitude() + " " + value.getLongitude());
        }

    }
}
