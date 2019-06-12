package com.tubeproject.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Insertable {
    PreparedStatement getInsertStatement() throws SQLException;

    String description();
}
