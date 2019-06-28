package com.tubeproject.model.interfaces;

import com.tubeproject.model.annotation.Description;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface Selectable {
    @Description("Select interface")
    PreparedStatement getSelectStatement() throws SQLException;

    Optional<?> buildFromResult(ResultSet resultSet);
}
