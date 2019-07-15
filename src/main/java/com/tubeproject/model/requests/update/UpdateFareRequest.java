package com.tubeproject.model.requests.update;

import com.tubeproject.controller.Fare;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Updatable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateFareRequest implements Updatable {

    private Fare fare;

    public UpdateFareRequest(Fare fare) {
        this.fare = fare;
    }

    @Description("Updating fares from database")
    @Override
    public PreparedStatement getUpdateStatement() throws SQLException {
        String query = "UPDATE `fares` SET %s=? WHERE zone_from=? AND zone_to=?";
        String valueToChange;
        switch (fare.getType()) {
            case ADULT:
                valueToChange = "ticket_adult";
                break;
            case CHILD:
                valueToChange = "ticket_child";
                break;
            case OYSTER_PEAK:
                valueToChange = "oyster_peak";
                break;
            case OYSTER_OFF_PEAK:
                valueToChange = "oyster_off_peak";
                break;
            default:
                valueToChange = "NONE";
        }
        query = String.format(query, valueToChange);
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setDouble(1, fare.getPrice());
        stmt.setInt(2, fare.getDepartingZone().getId());
        stmt.setInt(3, fare.getArrivingZone().getId());
        return stmt;
    }
}
