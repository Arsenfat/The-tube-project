package com.tubeproject.model.requests.select;

import com.tubeproject.controller.Station;
import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.builder.StationBuilder;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GetLastThreeTravelsRequest implements Selectable {

    private User user;

    public GetLastThreeTravelsRequest(User user) {
        this.user = user;
    }

    @Description("Get the last three travels frm a user from db")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT s1.naptan AS S1_NAPTAN, s1.name as S1_NAME, s1.latitude AS S1_LATITUDE, s1.longitude AS S1_LONGITUDE, s2.naptan AS S2_NAPTAN, s2.name AS S2_NAME, s2.latitude AS S2_LATITUDE, s2.longitude AS S2_LONGITUDE FROM `stations` AS s1 INNER JOIN `travels` AS t ON s1.naptan = t.starting_station INNER JOIN `stations` AS s2 ON s2.naptan = t.arriving_station WHERE t.user_id=(SELECT id FROM `users` WHERE email=?) ORDER BY id DESC LIMIT 3 ";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setString(1, user.getEmail());
        return stmt;
    }

    @Override
    public Optional<Map<String, List<Station>>> buildFromResult(ResultSet resultSet) {
        Map<String, List<Station>> travels = new HashMap<>();
        int i = 1;
        try {
            while (resultSet.next()) {
                String s1_naptan = resultSet.getString("S1_NAPTAN");
                String s1_name = resultSet.getString("S1_NAME");
                double s1_latitude = resultSet.getDouble("S1_LATITUDE");
                double s1_longitude = resultSet.getDouble("S1_LONGITUDE");

                Station s1 = new StationBuilder()
                        .setNaptan(s1_naptan)
                        .setName(s1_name)
                        .setLatitude(s1_latitude)
                        .setLongitude(s1_longitude)
                        .createStation();


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

                travels.put(String.format("histo%d", i), List.of(s1, s2));
                i++;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Optional.empty();
        }
        return Optional.of(travels);
    }
}
