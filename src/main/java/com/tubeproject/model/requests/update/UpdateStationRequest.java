package com.tubeproject.model.requests.update;

import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Updatable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateStationRequest implements Updatable {
    private Station station;

    public UpdateStationRequest(Station station) {
        this.station = station;
    }

    @Override
    @Description("Updating station")
    public PreparedStatement getUpdateStatement() throws SQLException {
        String query = "UPDATE `stations` SET name=?, wheelchair=?, latitude=?, longitude=? WHERE naptan=?";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setString(1, station.getName());
        stmt.setBoolean(2, station.isWheelchair());
        stmt.setDouble(3, station.getLatitude());
        stmt.setDouble(4, station.getLongitude());
        stmt.setString(5, station.getNaptan());
        return stmt;
    }
}
