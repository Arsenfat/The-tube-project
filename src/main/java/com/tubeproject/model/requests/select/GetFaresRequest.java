package com.tubeproject.model.requests.select;

import com.tubeproject.controller.Fare;
import com.tubeproject.controller.Zone;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.builder.FareBuilder;
import com.tubeproject.model.builder.ZoneBuilder;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetFaresRequest implements Selectable {

    @Description("Get fares from db")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT z1.id AS Z1_ID, z1.name AS Z1_NAME, z2.id AS Z2_ID, z2.name AS Z2_NAME, ticket_adult, ticket_child, oyster_peak, oyster_off_peak " +
                "FROM `zones` AS z1 " +
                "INNER JOIN `fares` AS f ON z1.id = f.zone_from " +
                "INNER JOIN `zones` AS z2 ON s2.naptan = f.zone_to";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        return stmt;
    }

    @Override
    public Optional<List<Fare>> buildFromResult(ResultSet resultSet) {
        List<Fare> fares = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String z1_name = resultSet.getString("Z1_NAME");
                int z1_id = resultSet.getInt("Z1_ID");

                String z2_name = resultSet.getString("Z2_NAME");
                int z2_id = resultSet.getInt("Z2_ID");

                double ticket_child = resultSet.getDouble("ticket_child");
                double ticket_adult = resultSet.getDouble("ticket_adult");
                double oyster_peak = resultSet.getDouble("oyster_peak");
                double oyster_off_peak = resultSet.getDouble("oyster_off_peak");

                Zone z1 = new ZoneBuilder().setId(z1_id).setName(z1_name).createZone();
                Zone z2 = new ZoneBuilder().setId(z2_id).setName(z2_name).createZone();

                Fare fare_child = new FareBuilder()
                        .setDepartingZone(z1)
                        .setArrivingZone(z2)
                        .setPrice(ticket_child)
                        .setType(Fare.Type.CHILD)
                        .createFare();

                Fare fare_adult = new FareBuilder()
                        .setDepartingZone(z1)
                        .setArrivingZone(z2)
                        .setPrice(ticket_adult)
                        .setType(Fare.Type.ADULT)
                        .createFare();

                Fare fare_oyster_p = new FareBuilder()
                        .setDepartingZone(z1)
                        .setArrivingZone(z2)
                        .setPrice(oyster_peak)
                        .setType(Fare.Type.OYSTER_PEAK)
                        .createFare();

                Fare fare_oyster_o = new FareBuilder()
                        .setDepartingZone(z1)
                        .setArrivingZone(z2)
                        .setPrice(oyster_off_peak)
                        .setType(Fare.Type.OYSTER_OFF_PEAK)
                        .createFare();

                fares.add(fare_child);
                fares.add(fare_adult);
                fares.add(fare_oyster_p);
                fares.add(fare_oyster_o);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Optional.empty();
        }
        return Optional.of(fares);
    }
}
