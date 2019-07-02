package com.tubeproject.model.requests;

import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GetAllStationsRequest implements Selectable {


    @Description("Get all stations as a list")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT naptan, name, latitude, longitude FROM stations";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        return stmt;
    }

    @Override
    public Optional<List<Station>> buildFromResult(ResultSet resultSet) {
        return RequestUtils.buildListFromRS(resultSet);
    }
}
