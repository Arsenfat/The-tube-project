package com.tubeproject.core;

import com.tubeproject.algorithm.PathCalculator;
import com.tubeproject.model.ContextMap;
import com.tubeproject.view.user.ViewMainScreen;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        //Data Initialization
        Thread t = new Thread(PathCalculator::initializeGraphs);
        t.start();

        //Toujours initialiser la context map
        Map<String, Object> ctxMap = ContextMap.getContextMap();
        ViewMainScreen.startWindow();
        /*
        StationWLine s1 = PathCalculator.getTravelEdges().keySet().stream().filter(station -> station.getNaptan().equalsIgnoreCase("940GZZLUWIG") && station.getLine().getId() == 6).findFirst().get();
        StationWLine s2 = PathCalculator.getTravelEdges().keySet().stream().filter(station -> station.getNaptan().equalsIgnoreCase("940GZZLUBWT") && station.getLine().getId() == 3).findFirst().get();


        PathCalculator.calculateQuickest(s1, s2).forEach(System.out::println);

        System.out.println("\n\n\n");

        PathCalculator.calculateLessConnection(s1, s2).forEach(System.out::println);*/


    }

}
