package com.tubeproject.model.requests;

import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Select {
    private Selectable selectable;
    private String description = "none";

    public Select(Selectable s) {

        selectable = s;
        if (selectable.getClass().getAnnotation(Description.class) != null)
            description = selectable.getClass().getAnnotation(Description.class).value();
    }

    public Optional<?> select() {
        ResultSet resultSet;
        PreparedStatement stmt;

        try {
            stmt = selectable.getSelectQuery();
        } catch (SQLException e) {

            System.out.println(String.format("Error while preparing stmt -> %s", this.description));
            System.out.println(e);
            return Optional.empty();
        }

        try {
            stmt.setQueryTimeout(2);
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