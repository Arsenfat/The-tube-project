package com.tubeproject.core;

import com.tubeproject.controller.*;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.builder.ConnectionWLineBuilder;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.select.GetAllConnectionsRequest;
import com.tubeproject.model.requests.select.GetAllConnectionsWLineRequest;
import com.tubeproject.model.requests.select.GetAllLinesWithStationsRequest;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        //Toujours initialiser la context map
        //Map<String, Object> ctxMap = ContextMap.getContextMap();
        //ViewMainScreen.startWindow();
        //Data Initialization
        Map<Station, List<Connection>> dataQuickest = getData();

        DefaultUndirectedWeightedGraph<Station, DefaultWeightedEdge> quickestRoute = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
        fillVertexesQuickest(quickestRoute, new ArrayList<>(dataQuickest.keySet()));
        System.out.println("QUICKEST nb vertexe" + quickestRoute.vertexSet().size());
        fillEdgesQuickest(quickestRoute, dataQuickest);
        System.out.println("QUICKEST nb Edges" + quickestRoute.edgeSet().size());
        //Research
        Station s1 = dataQuickest.keySet().stream().filter(station -> station.getName().equalsIgnoreCase("Holborn")).findFirst().get();
        Station s2 = dataQuickest.keySet().stream().filter(station -> station.getName().equalsIgnoreCase("West Finchley")).findFirst().get();
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(quickestRoute);
        List<Station> shortestPath = dijkstraShortestPath
                .getPath(s1, s2).getVertexList();
        shortestPath.forEach(System.out::println);


        List<Line> listLine = new ArrayList<>();
        try {
            DatabaseConnection.DatabaseOpen();
            GetAllLinesWithStationsRequest getAllLinesWithStationsRequest = new GetAllLinesWithStationsRequest();
            Select s = new Select(getAllLinesWithStationsRequest);
            listLine = (List<Line>) s.select().get();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
        }

        Map<StationWLine, List<ConnectionWLine>> travelEdges = getDataLessConnection();
        Map<StationWLine, List<ConnectionWLine>> correspondanceEdges = getLineConnections(listLine, new ArrayList<>(travelEdges.keySet()));
        //Data Initialization
        DefaultUndirectedWeightedGraph<StationWLine, DefaultWeightedEdge> lessConnection = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
        fillVertexesLessConnection(lessConnection, new ArrayList<>(travelEdges.keySet()));
        System.out.println("LESS nb vertexe" + lessConnection.vertexSet().size());
        fillEdgesLessConnection(lessConnection, travelEdges);
        System.out.println("LESS nb edges" + lessConnection.edgeSet().size());
        fillEdgesLessConnection(lessConnection, correspondanceEdges);
        System.out.println("LESS nb edges" + lessConnection.edgeSet().size());


        StationWLine s1less = travelEdges.keySet().stream().filter(station -> station.getNaptan().equalsIgnoreCase("940GZZLUHBN") && station.getLine().getId() == 9).findFirst().get();
        StationWLine s2less = travelEdges.keySet().stream().filter(station -> station.getNaptan().equalsIgnoreCase("940GZZLUWFN") && station.getLine().getId() == 8).findFirst().get();


        DijkstraShortestPath<StationWLine, DefaultWeightedEdge> dijkstraShortestPathLess
                = new DijkstraShortestPath<>(lessConnection);

        List<StationWLine> shortestPathLess = dijkstraShortestPathLess.getPath(s1less, s2less).getVertexList();
        shortestPathLess.forEach(System.out::println);

    }

    private static Map<StationWLine, List<ConnectionWLine>> getDataLessConnection() {
        Map<StationWLine, List<ConnectionWLine>> map = new HashMap<>();
        try {
            DatabaseConnection.DatabaseOpen();
            GetAllConnectionsWLineRequest getAllConnectionsWLineRequest = new GetAllConnectionsWLineRequest();
            Select s = new Select(getAllConnectionsWLineRequest);
            map = (Map<StationWLine, List<ConnectionWLine>>) s.select().get();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
            System.exit(250);
        }
        return map;
    }


    private static Map<StationWLine, List<ConnectionWLine>> getLineConnections(List<Line> listLine, List<StationWLine> stationWLineList) {
        double weight = 15;
        Map<StationWLine, List<ConnectionWLine>> map = new HashMap<>();
        for (Line current : listLine) {
            for (Line toTest : listLine) {
                if (current.equals(toTest)) {
                    continue;
                }
                for (Station currentStation : current.getStations()) {
                    for (Station toTestStation : toTest.getStations()) {
                        if (currentStation.getLatitude() == toTestStation.getLatitude() && currentStation.getLongitude() == toTestStation.getLongitude()) {
                            StationWLine toTestStationWLine = stationWLineList.stream().filter(tmp -> tmp.getNaptan().equalsIgnoreCase(toTestStation.getNaptan()) && tmp.getLine().getId() == toTest.getId()).findFirst().orElse(null);
                            StationWLine currentStationWline = stationWLineList.stream().filter(tmp -> tmp.getNaptan().equalsIgnoreCase(currentStation.getNaptan()) && tmp.getLine().getId() == current.getId()).findFirst().orElse(null);
                            if (toTestStationWLine != null && currentStationWline != null) {
                                List<ConnectionWLine> connectionList = map.computeIfAbsent(currentStationWline, k -> new ArrayList<>());
                                connectionList.add(new ConnectionWLineBuilder().setStation(toTestStationWLine).setDuration(weight).createConnectionWLine());
                                List<ConnectionWLine> connectionList2 = map.computeIfAbsent(toTestStationWLine, k -> new ArrayList<>());
                                connectionList2.add(new ConnectionWLineBuilder().setStation(currentStationWline).setDuration(weight).createConnectionWLine());
                            }
                        }
                    }
                }
            }
        }
        //Cleaning duplicates
        for (Map.Entry<StationWLine, List<ConnectionWLine>> entry : map.entrySet()) {
            map.put(entry.getKey(), entry.getValue().stream().distinct().collect(Collectors.toList()));
        }


        return map;
    }

    private static Map<Station, List<Connection>> getData() {
        Map<Station, List<Connection>> map = new HashMap<>();
        try {
            DatabaseConnection.DatabaseOpen();
            GetAllConnectionsRequest getAllConnectionsRequest = new GetAllConnectionsRequest();
            Select s = new Select(getAllConnectionsRequest);
            map = (Map<Station, List<Connection>>) s.select().get();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
            System.exit(250);
        }
        return map;
    }

    private static void fillVertexesQuickest(DefaultUndirectedWeightedGraph<Station, DefaultWeightedEdge> graph, List<Station> list) {
        for (Station station : list) {
            graph.addVertex(station);
        }
    }

    private static void fillVertexesLessConnection(DefaultUndirectedWeightedGraph<StationWLine, DefaultWeightedEdge> graph, List<StationWLine> list) {
        for (StationWLine station : list) {
            graph.addVertex(station);
        }
    }


    private static void fillEdgesQuickest(DefaultUndirectedWeightedGraph<Station, DefaultWeightedEdge> graph, Map<Station, List<Connection>> map) {
        for (Map.Entry<Station, List<Connection>> entry : map.entrySet()) {
            Station s = entry.getKey();
            for (Connection con : entry.getValue()) {
                DefaultWeightedEdge edge = new DefaultWeightedEdge();
                if (graph.addEdge(s, con.getStation(), edge)) {
                    graph.setEdgeWeight(edge, con.getDuration());
                }
            }
        }
    }

    private static void fillEdgesLessConnection(DefaultUndirectedWeightedGraph<StationWLine, DefaultWeightedEdge> graph, Map<StationWLine, List<ConnectionWLine>> map) {
        for (Map.Entry<StationWLine, List<ConnectionWLine>> entry : map.entrySet()) {
            StationWLine s = entry.getKey();
            for (ConnectionWLine con : entry.getValue()) {
                DefaultWeightedEdge edge = new DefaultWeightedEdge();
                if (graph.addEdge(s, con.getStation(), edge)) {
                    graph.setEdgeWeight(edge, con.getDuration());
                }
            }
        }

    }

}
