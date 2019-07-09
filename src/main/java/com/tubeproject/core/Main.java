package com.tubeproject.core;

import com.tubeproject.controller.Station;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Select;
import com.tubeproject.model.requests.GetAllConnections;
import com.tubeproject.view.ViewMainScreen;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        //Toujours initialiser la context map
        Map<String, Object> ctxMap = ContextMap.getContextMap();
        DatabaseConnection.DatabaseOpen();
        GetAllConnections gac = new GetAllConnections();
        Select s = new Select(gac);
        Map<Station, List<Connection>> map = (Map<Station, List<Connection>>) s.select().get();
        System.out.println(map);
        DatabaseConnection.DatabaseOpen();
        ViewMainScreen.startWindow();
    }

}
