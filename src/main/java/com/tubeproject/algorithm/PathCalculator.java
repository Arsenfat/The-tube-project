package com.tubeproject.algorithm;

import com.tubeproject.controller.ConnectionWLine;
import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import com.tubeproject.controller.StationWLine;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.builder.ConnectionWLineBuilder;
import com.tubeproject.model.requests.Select;
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

public class PathCalculator {
    private static Map<StationWLine, List<ConnectionWLine>> noCorrespondEdges;

    private static DefaultUndirectedWeightedGraph<StationWLine, DefaultWeightedEdge> quickestGraph;
    private static DefaultUndirectedWeightedGraph<StationWLine, DefaultWeightedEdge> lessConnectionsGraph;

    private static DijkstraShortestPath<StationWLine, DefaultWeightedEdge> dijkstraQuickest;
    private static DijkstraShortestPath<StationWLine, DefaultWeightedEdge> dijkstraLessConnection;

    private PathCalculator() {
        //we don't instantiate
    }

    public static void initializeGraphs() {
        try {
            DatabaseConnection.DatabaseOpen();
            //Data in common in both graphs
            List<Line> lineList = loadLines();
            noCorrespondEdges = getConnectionEdge();
            List<StationWLine> verticesList = new ArrayList<>(noCorrespondEdges.keySet());

            //Quickest path graph initialization, weight of 2 on line change
            quickestGraph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
            Map<StationWLine, List<ConnectionWLine>> correspEdgesQuickest = getCorrespondanceEdges(lineList, verticesList, 3);
            fillVertexes(quickestGraph, verticesList);
            fillEdges(quickestGraph, noCorrespondEdges);
            fillEdges(quickestGraph, correspEdgesQuickest);
            dijkstraQuickest = new DijkstraShortestPath<>(quickestGraph);


            lessConnectionsGraph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
            Map<StationWLine, List<ConnectionWLine>> correspEdgesLessConnection = getCorrespondanceEdges(lineList, verticesList, 15);
            fillVertexes(lessConnectionsGraph, verticesList);
            fillEdges(lessConnectionsGraph, noCorrespondEdges);
            fillEdges(lessConnectionsGraph, correspEdgesLessConnection);
            dijkstraLessConnection = new DijkstraShortestPath<>(lessConnectionsGraph);
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static PathResponse calculatePath(StationWLine s1, StationWLine s2) {
        PathResponse pathResponse = mergeResponses(calculateQuickest(s1, s2), calculateLessConnection(s1, s2));
        return pathResponse;
    }


    private static PathResponse mergeResponses(PathResponse p1, PathResponse p2) {
        PathResponse pFinal = new PathResponse();
        if (p1.getQuickest() != null) {
            pFinal.setQuickest(p1.getQuickest());
            pFinal.setLessConnection(p2.getLessConnection());
        } else {
            pFinal.setQuickest(p2.getQuickest());
            pFinal.setLessConnection(p1.getLessConnection());
        }
        return pFinal;
    }

    public static PathResponse calculateQuickest(StationWLine s1, StationWLine s2) {
        return new PathResponse(dijkstraQuickest.getPath(s1, s2).getVertexList(), null);
    }

    public static PathResponse calculateLessConnection(StationWLine s1, StationWLine s2) {
        return new PathResponse(null, dijkstraLessConnection.getPath(s1, s2).getVertexList());
    }

    public static Map<StationWLine, List<ConnectionWLine>> getTravelEdges() {
        return noCorrespondEdges;
    }

    private static Map<StationWLine, List<ConnectionWLine>> getConnectionEdge() {
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

    private static Map<StationWLine, List<ConnectionWLine>> getCorrespondanceEdges(List<Line> listLine, List<StationWLine> stationWLineList, int weight) {
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

    private static List<Line> loadLines() {
        List<Line> lineList = new ArrayList<>();
        try {
            DatabaseConnection.DatabaseOpen();
            GetAllLinesWithStationsRequest getAllLinesWithStationsRequest = new GetAllLinesWithStationsRequest();
            Select s = new Select(getAllLinesWithStationsRequest);
            lineList = (List<Line>) s.select().get();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return lineList;
    }


    private static void fillVertexes(DefaultUndirectedWeightedGraph<StationWLine, DefaultWeightedEdge> graph, List<StationWLine> list) {
        for (StationWLine station : list) {
            graph.addVertex(station);
        }
    }

    private static void fillEdges(DefaultUndirectedWeightedGraph<StationWLine, DefaultWeightedEdge> graph, Map<StationWLine, List<ConnectionWLine>> map) {
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
