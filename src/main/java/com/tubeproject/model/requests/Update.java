package com.tubeproject.model.requests;

import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Updatable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update {
    private Updatable updatable;
    private String description = "none";

    public Update(Updatable u) {

        updatable = u;
        System.out.println(updatable.getClass());
        if (updatable.getClass().getAnnotation(Description.class) != null)
            description = updatable.getClass().getAnnotation(Description.class).value();
    }

    public void update() {
        Connection connection = DatabaseConnection.getCon();
        PreparedStatement stmt;

        try {
            stmt = updatable.getUpdateStatement();
        } catch (SQLException e) {
            System.out.println(String.format("Error while preparing stmt -> %s", this.description));
            System.out.println(e);
            return;
        }

        try {
            stmt.setQueryTimeout(2);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(String.format("Error while executing stmt -> %s", this.description));
            System.out.println(e);
        } finally {
            try {
                connection.commit();
                stmt.close();
            } catch (SQLException e) {
                System.out.println(String.format("Error while closing stmt -> %s", this.description));
                System.out.println(e);

            }
        }
    }
}
