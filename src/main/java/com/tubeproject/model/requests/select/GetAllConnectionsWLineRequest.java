package com.tubeproject.model.requests.select;

import com.tubeproject.controller.ConnectionWLine;
import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import com.tubeproject.controller.StationWLine;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.builder.ConnectionWLineBuilder;
import com.tubeproject.model.builder.LineBuilder;
import com.tubeproject.model.builder.StationBuilder;
import com.tubeproject.model.interfaces.Selectable;
import com.tubeproject.model.requests.Select;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GetAllConnectionsWLineRequest implements Selectable {
    @Description("Load all the connections with line from the grid")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT s1.naptan AS S1_NAPTAN, s1.name as S1_NAME, s1.wheelchair AS S1_WHEELCHAIR, s1.latitude AS S1_LATITUDE, s1.longitude AS S1_LONGITUDE, s2.naptan AS S2_NAPTAN, s2.name AS S2_NAME, s2.wheelchair AS S2_WHEELCHAIR, s2.latitude AS S2_LATITUDE, s2.longitude AS S2_LONGITUDE, duration, l.id AS LINE_ID, l.name AS LINE_NAME " +
                "FROM `stations` AS s1 " +
                "INNER JOIN `station_line_durations` AS d ON s1.naptan = d.station_departing " +
                "INNER JOIN `stations` AS s2 ON s2.naptan = d.station_arriving " +
                "INNER JOIN `lines` AS l ON l.id=d.line;";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        return stmt;
    }

    @Override
    public Optional<Map<StationWLine, List<ConnectionWLine>>> buildFromResult(ResultSet resultSet) {
        Map<StationWLine, List<ConnectionWLine>> connectionGrid = new HashMap<>();
        Map<Integer, Line> bufferLine = new HashMap<>();
        try {
            while (resultSet.next()) {
                int line_id = resultSet.getInt("LINE_ID");
                String line_name = resultSet.getString("LINE_NAME");

                Line line = bufferLine.computeIfAbsent(line_id, id -> {
                    Line tmp = new LineBuilder()
                            .setId(line_id)
                            .setName(line_name)
                            .createLine();
                    GetStationsFromLineRequest getStationsFromLineRequest = new GetStationsFromLineRequest(tmp);
                    Select s = new Select(getStationsFromLineRequest);
                    Optional<?> opt = s.select();
                    if (opt.isPresent()) {
                        tmp.setStations((List<Station>) opt.get());
                    } else {
                        System.out.println("Grosse erreur");
                        System.exit(63);
                    }
                    return tmp;
                });

                String s1_naptan = resultSet.getString("S1_NAPTAN");
                String s1_name = resultSet.getString("S1_NAME");
                double s1_latitude = resultSet.getDouble("S1_LATITUDE");
                double s1_longitude = resultSet.getDouble("S1_LONGITUDE");
                boolean s1_wheelchair = resultSet.getBoolean("S1_WHEELCHAIR");

                StationWLine s1 = new StationWLine(new StationBuilder()
                        .setNaptan(s1_naptan)
                        .setName(s1_name)
                        .setLatitude(s1_latitude)
                        .setLongitude(s1_longitude)
                        .setWheelchair(s1_wheelchair)
                        .createStation(), line);


                String s2_naptan = resultSet.getString("S2_NAPTAN");
                String s2_name = resultSet.getString("S2_NAME");
                double s2_latitude = resultSet.getDouble("S2_LATITUDE");
                double s2_longitude = resultSet.getDouble("S2_LONGITUDE");
                boolean s2_wheelchair = resultSet.getBoolean("S2_WHEELCHAIR");

                StationWLine s2 = new StationWLine(new StationBuilder()
                        .setNaptan(s2_naptan)
                        .setName(s2_name)
                        .setLatitude(s2_latitude)
                        .setLongitude(s2_longitude)
                        .setWheelchair(s2_wheelchair)
                        .createStation(),
                        line);

                List<ConnectionWLine> connectionList = connectionGrid.computeIfAbsent(s1, key -> new ArrayList<>());
                List<ConnectionWLine> connectionList2 = connectionGrid.computeIfAbsent(s2, key -> new ArrayList<>());

                double duration = resultSet.getDouble("duration");

                ConnectionWLine connection = new ConnectionWLineBuilder()
                        .setStation(s2)
                        .setDuration(duration)
                        .createConnectionWLine();

                ConnectionWLine connection2 = new ConnectionWLineBuilder()
                        .setStation(s1)
                        .setDuration(duration)
                        .createConnectionWLine();

                //Stations have two directions
                //Ex Finchley Road to Swiss Cottage
                //   Swiss Cottage to Finchley Road
                //We assume the duration is the same in the two directions
                connectionList.add(connection);
                connectionList2.add(connection2);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Optional.empty();
        }

        return Optional.of(connectionGrid);
    }
}
