package com.tubeproject.model.requests.select;

import com.tubeproject.controller.Station;
import com.tubeproject.controller.Zone;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.builder.ZoneBuilder;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetZoneFromStationRequest implements Selectable {

    private Station station;

    public GetZoneFromStationRequest(Station station) {
        this.station = station;
    }

    @Description("Get the zones of a station from db")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT id, name FROM zones z INNER JOIN zones_stations zs ON z.id=zs.zone WHERE station=?";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setString(1, station.getNaptan());
        return stmt;
    }

    @Override
    public Optional<List<Zone>> buildFromResult(ResultSet resultSet) {
        List<Zone> zones = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                Zone tmp = new ZoneBuilder()
                        .setName(name)
                        .setId(id)
                        .createZone();
                zones.add(tmp);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Optional.empty();
        }
        return Optional.of(zones);
    }
}
