package com.tubeproject.model.requests.select;

import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ComputeNumberTravelsRequest implements Selectable {
    @Description("Compute the number of travels")
    @Override
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT COUNT(*) AS NB_TRAVELS FROM `travels` ";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        return stmt;
    }

    @Override
    public Optional<Integer> buildFromResult(ResultSet resultSet) {
        int nbTravels = 0;
        try {
            if (resultSet.next()) {
                nbTravels = resultSet.getInt("NB_TRAVELS");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.of(nbTravels);
    }
}
