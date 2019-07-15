package com.tubeproject.model.requests;

import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Insertable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Insert {
    private Insertable insertable;
    private String description = "none";
    private int id = -1;

    public Insert(Insertable i) {
        insertable = i;
        try {
            description = insertable.getClass().getMethod("getInsertStatement").getAnnotation(Description.class).value();
        } catch (NoSuchMethodException e) {
            System.out.println(e);
        }
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
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(String.format("Error while executing stmt -> %s", this.description));
            System.out.println(e);
            return;
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.id = (int) generatedKeys.getLong(1);
            }
            connection.commit();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(String.format("Error while closing stmt -> %s", this.description));
            System.out.println(e);

        }
    }

    public int getId() {
        return this.id;
    }
}
