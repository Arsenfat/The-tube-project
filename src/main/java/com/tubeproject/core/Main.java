package com.tubeproject.core;

import com.tubeproject.model.ContextMap;
import com.tubeproject.view.ViewMainScreen;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        //Toujours initialiser la context map
        Map<String, Object> ctxMap = ContextMap.getContextMap();
        ViewMainScreen.startWindow();
    }

}
