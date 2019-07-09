package com.tubeproject.model.requests;

import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Select;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.builder.LineBuilder;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetAllLinesWithStationsRequest implements Selectable {

    @Description("Load all the lines with their respective stations")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT id, name FROM `lines`";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        return stmt;
    }

    @Override
    public Optional<List<Line>> buildFromResult(ResultSet resultSet) {
        List<Line> lines = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                LineBuilder tmp = new LineBuilder()
                        .setName(name)
                        .setId(id);

                GetStationsFromLineRequest getStationsFromLineRequest = new GetStationsFromLineRequest(tmp.createLine());
                Select s = new Select(getStationsFromLineRequest);

                List<Station> stations = (List<Station>) s.select().get();
                tmp.setStations(stations);
                lines.add(tmp.createLine());
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Optional.empty();
        }
        return Optional.of(lines);
    }
}
