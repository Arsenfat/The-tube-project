package com.tubeproject.model.interfaces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface Selectable {
    PreparedStatement getSelectQuery() throws SQLException;

    Optional<?> buildFromResult(ResultSet resultSet);

    String description();
}
