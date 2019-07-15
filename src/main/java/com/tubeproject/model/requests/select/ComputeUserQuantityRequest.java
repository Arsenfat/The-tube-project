package com.tubeproject.model.requests.select;

import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ComputeUserQuantityRequest implements Selectable {

    @Description("Compute the number of users registered")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT COUNT(*) AS NB_USERS FROM `users` ";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        return stmt;
    }

    @Override
    public Optional<Integer> buildFromResult(ResultSet resultSet) {
        int nbUsers = 0;
        try {
            if (resultSet.next()) {
                nbUsers = resultSet.getInt("NB_USERS");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.of(nbUsers);
    }
}
