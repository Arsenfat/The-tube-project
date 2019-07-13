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

    }

}
