package com.tubeproject.core;

import com.tubeproject.algorithm.PathCalculator;
import com.tubeproject.algorithm.PathResponse;
import com.tubeproject.controller.StationWLine;
import com.tubeproject.model.ContextMap;
import com.tubeproject.view.user.ViewMainScreen;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        //Data Initialization
        Thread t = new Thread(PathCalculator::initializeGraphs);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        StationWLine s1 = PathCalculator.getTravelEdges().keySet().stream().filter(station -> station.getNaptan().equalsIgnoreCase("940GZZLUWIG") && station.getLine().getId() == 6).findFirst().get();
        StationWLine s2 = PathCalculator.getTravelEdges().keySet().stream().filter(station -> station.getNaptan().equalsIgnoreCase("940GZZLUBWT") && station.getLine().getId() == 3).findFirst().get();

        PathResponse pR = PathCalculator.calculatePath(s1, s2);

        pR.getQuickest().forEach(System.out::println);
        System.out.println("\n\n\n");
        pR.getLessConnection().forEach(System.out::println);


        //Toujours initialiser la context map
        Map<String, Object> ctxMap = ContextMap.getContextMap();
        ViewMainScreen.startWindow();

    }

}
