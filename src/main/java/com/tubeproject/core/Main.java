package com.tubeproject.core;

import com.tubeproject.algorithm.PathCalculator;
import com.tubeproject.model.ContextMap;
import com.tubeproject.view.user.ViewMainScreen;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        //Data Initialization
        CentralPooling.execute(PathCalculator::initializeGraphs);

        //Always initialize context map
        Map<String, Object> ctxMap = ContextMap.getContextMap();
        ViewMainScreen.startWindow();

    }

}
