package com.tubeproject.model.requests.select;

import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GetStationsFromLineRequest implements Selectable {

    private Line line;

    public GetStationsFromLineRequest(Line line) {
        this.line = line;
    }

    @Description("Get all stations on a line")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT naptan, s.name, latitude, longitude FROM `lines` l, `line_stations` k JOIN `stations` s ON s.naptan=k.station WHERE l.id=k.line AND l.id=?";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setInt(1, line.getId());
        return stmt;
    }

    @Override
    public Optional<List<Station>> buildFromResult(ResultSet resultSet) {
        return RequestUtils.buildListFromRS(resultSet);
    }
}
