package com.tubeproject.model.requests.insert;

import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Insertable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertStationsTravelRequest implements Insertable {

    private int id;
    private Station station;

    public InsertStationsTravelRequest(int id, Station station) {
        this.id = id;
        this.station = station;
    }

    @Override
    @Description("Insert each station of a travel into Database")
    public PreparedStatement getInsertStatement() throws SQLException {
        String query = "INSERT INTO `histories`(travel_id,station) VALUES(?,?);";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setInt(1, id);
        stmt.setString(2, station.getNaptan());
        return stmt;
    }

}
