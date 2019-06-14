package com.tubeproject.model;

import com.tubeproject.model.interfaces.Insertable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {
    private Insertable insertable;

    public Insert(Insertable i) {
        insertable = i;
    }

    public void insert() {
        Connection connection = DatabaseConnection.getCon();
        PreparedStatement stmt;

        try {
            stmt = insertable.getInsertStatement();
        } catch (SQLException e) {
            System.out.println("Error while preparing stmt -> " + insertable.description());
            System.out.println(e);
            return;
        }

        try {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Error while executing stmt -> " + insertable.description());
            System.out.println(e);
            return;
        }

        try {
            connection.commit();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error while closing stmt -> " + insertable.description());
            System.out.println(e);

        }
    }
}
