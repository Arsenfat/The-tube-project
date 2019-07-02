package com.tubeproject.model.requests;

import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.builder.StationBuilder;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetAllStations implements Selectable {


    @Description("Get all stations as a list")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT naptan, name, latitude, longitude FROM stations";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        return stmt;
    }

    @Override
    public Optional<List<Station>> buildFromResult(ResultSet resultSet) {
        List<Station> stations = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String naptan = resultSet.getString("naptan");
                String name = resultSet.getString("name");
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");

                Station tmp = new StationBuilder()
                        .setNaptan(naptan)
                        .setName(name)
                        .setLatitude(latitude)
                        .setLongitude(longitude)
                        .createStation();
                stations.add(tmp);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Optional.empty();
        }
        return Optional.of(stations);
    }
}
