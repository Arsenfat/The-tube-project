package com.tubeproject.model;

import java.util.HashMap;
import java.util.Map;

public class ContextMap {
    private static Map<String, Object> contextMap;

    private ContextMap() {

    }

    public static Map<String, Object> getContextMap() {
        if (contextMap == null) {
            contextMap = new HashMap<>();
        }
        return contextMap;
    }

}
