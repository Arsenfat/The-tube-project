package com.tubeproject.model.requests.update;

import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Updatable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdatePasswordRequest implements Updatable {

    private User user;

    public UpdatePasswordRequest(User user) {
        this.user = user;
    }

    @Override
    @Description("update password from user")
    public PreparedStatement getUpdateStatement() throws SQLException {
        String query = "UPDATE `users` SET password=?, salt=? WHERE email=?";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setString(1, user.getPassword());
        stmt.setString(2, user.getSalt());
        stmt.setString(3, user.getEmail());
        return stmt;
    }
}
