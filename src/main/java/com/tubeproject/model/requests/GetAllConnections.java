package com.tubeproject.model.requests;

import com.tubeproject.controller.Connection;
import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.builder.ConnectionBuilder;
import com.tubeproject.model.builder.StationBuilder;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GetAllConnections implements Selectable {
    @Description("Load all the connections from the grid")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT s1.naptan AS S1_NAPTAN, s1.name as S1_NAME, s1.latitude AS S1_LATITUDE, s1.longitude AS S1_LONGITUDE, s2.naptan AS S2_NAPTAN, s2.name AS S2_NAME, s2.latitude AS S2_LATITUDE, s2.longitude AS S2_LONGITUDE, duration " +
                "FROM `stations` AS s1 " +
                "INNER JOIN `station_durations` AS d ON s1.naptan = d.station_departing " +
                "INNER JOIN `stations` AS s2 ON s2.naptan = d.station_arriving";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        return stmt;
    }

    @Override
    public Optional<Map<Station, List<Connection>>> buildFromResult(ResultSet resultSet) {
        Map<Station, List<Connection>> connectionGrid = new HashMap<>();

        try {
            while (resultSet.next()) {
                String s1_naptan = resultSet.getString("S1_NAPTAN");
                String s1_name = resultSet.getString("S1_NAME");
                double s1_latitude = resultSet.getDouble("S1_LATITUDE");
                double s1_longitude = resultSet.getDouble("S2_LONGITUDE");

                Station s1 = new StationBuilder()
                        .setNaptan(s1_naptan)
                        .setName(s1_name)
                        .setLatitude(s1_latitude)
                        .setLongitude(s1_longitude)
                        .createStation();

                List<Connection> connectionList = connectionGrid.computeIfAbsent(s1, k -> new ArrayList<>());

                String s2_naptan = resultSet.getString("S2_NAPTAN");
                String s2_name = resultSet.getString("S2_NAME");
                double s2_latitude = resultSet.getDouble("S2_LATITUDE");
                double s2_longitude = resultSet.getDouble("S2_LONGITUDE");
                Station s2 = new StationBuilder()
                        .setNaptan(s2_naptan)
                        .setName(s2_name)
                        .setLatitude(s2_latitude)
                        .setLongitude(s2_longitude)
                        .createStation();

                double duration = resultSet.getDouble("duration");

                Connection connection = new ConnectionBuilder()
                        .setStation(s2)
                        .setDuration(duration)
                        .createConnection();

                connectionList.add(connection);

            }
        } catch (SQLException e) {
            System.out.println(e);
            return Optional.empty();
        }
        return Optional.of(connectionGrid);
    }
}
