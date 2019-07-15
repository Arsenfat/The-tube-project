package com.tubeproject.model.requests.insert;

import com.tubeproject.controller.Station;
import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Insertable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertTravelRequest implements Insertable {
    private User user;
    private Station start;
    private Station end;

    public InsertTravelRequest(User user, Station start, Station end) {
        this.user = user;
        this.start = start;
        this.end = end;
    }


    @Override
    @Description("Insert new travel into Database")
    public PreparedStatement getInsertStatement() throws SQLException {
        String query = "INSERT INTO `travels`(starting_station,arriving_station,user_id) VALUES(?,?,(SELECT id FROM `users` WHERE email=?));";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setString(1, start.getNaptan());
        stmt.setString(2, end.getNaptan());
        stmt.setString(3, user.getEmail());
        return stmt;
    }
}
