package com.tubeproject.model;

import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Select {
    private Selectable selectable;
    private String description;

    public Select(Selectable s) {

        selectable = s;
        description = selectable.getClass().getAnnotation(Description.class).value();
    }

    public Optional<?> select() {
        ResultSet resultSet;
        PreparedStatement stmt;

        try {
            stmt = selectable.getSelectStatement();
        } catch (SQLException e) {

            System.out.println(String.format("Error while preparing stmt -> %s", this.description));
            System.out.println(e);
            return Optional.empty();
        }

        try {
            resultSet = stmt.executeQuery();
            return selectable.buildFromResult(resultSet);
        } catch (SQLException e) {
            System.out.println(String.format("Error while executing stmt -> %s", this.description));
            System.out.println(e);
            return Optional.empty();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println(String.format("Error while closing stmt -> %s", this.description));
                System.out.println(e);

            }
        }
    }


}