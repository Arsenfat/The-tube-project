package com.tubeproject.model;

import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Insertable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {
    private Insertable insertable;
    private String description;

    public Insert(Insertable i) {
        insertable = i;
        description = insertable.getClass().getAnnotation(Description.class).value();

    }

    public void insert() {
        Connection connection = DatabaseConnection.getCon();
        PreparedStatement stmt;

        try {
            stmt = insertable.getInsertStatement();
        } catch (SQLException e) {
            System.out.println(String.format("Error while preparing stmt -> %s", this.description));
            System.out.println(e);
            return;
        }

        try {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(String.format("Error while executing stmt -> %s", this.description));
            System.out.println(e);
            return;
        }

        try {
            connection.commit();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(String.format("Error while closing stmt -> %s", this.description));
            System.out.println(e);

        }
    }
}
