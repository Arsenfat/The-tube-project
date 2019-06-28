package com.tubeproject.model.interfaces;

import com.tubeproject.model.annotation.Description;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Insertable {
    @Description("Insert interface")
    PreparedStatement getInsertStatement() throws SQLException;

}
