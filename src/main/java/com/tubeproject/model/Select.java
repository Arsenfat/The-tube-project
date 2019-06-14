package com.tubeproject.model;

import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Select {
    private Selectable selectable;

    public Select(Selectable s) {
        selectable = s;
    }

    public Optional<?> select() {
        ResultSet resultSet;
        PreparedStatement stmt;

        try {
            stmt = selectable.getSelectQuery();
        } catch (SQLException e) {
            System.out.println("Error while preparing stmt -> " + selectable.description());
            System.out.println(e);
            return Optional.empty();
        }

        try {
            resultSet = stmt.executeQuery();
            return selectable.buildFromResult(resultSet);
        } catch (SQLException e) {
            System.out.println("Error while executing stmt -> " + selectable.description());
            System.out.println(e);
            return Optional.empty();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Error while closing stmt -> " + selectable.description());
                System.out.println(e);

            }
        }
    }


}