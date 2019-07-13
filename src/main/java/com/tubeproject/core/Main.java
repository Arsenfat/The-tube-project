package com.tubeproject.core;

import com.tubeproject.algorithm.PathCalculator;
import com.tubeproject.controller.ConnectionWLine;
import com.tubeproject.controller.StationWLine;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.select.GetAllConnectionsWLineRequest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        //Toujours initialiser la context map
        //Map<String, Object> ctxMap = ContextMap.getContextMap();
        //ViewMainScreen.startWindow();
        //Data Initialization
        PathCalculator.initializeGraphs();

        StationWLine s1 = PathCalculator.getTravelEdges().keySet().stream().filter(station -> station.getNaptan().equalsIgnoreCase("940GZZLUWIG") && station.getLine().getId() == 6).findFirst().get();
        StationWLine s2 = PathCalculator.getTravelEdges().keySet().stream().filter(station -> station.getNaptan().equalsIgnoreCase("940GZZLUBWT") && station.getLine().getId() == 3).findFirst().get();


        PathCalculator.calculateQuickest(s1, s2).forEach(System.out::println);

        System.out.println("\n\n\n");

        PathCalculator.calculateLessConnection(s1, s2).forEach(System.out::println);


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

}
