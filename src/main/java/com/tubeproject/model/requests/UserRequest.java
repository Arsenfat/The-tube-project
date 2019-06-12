package com.tubeproject.model.requests;

import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Insertable;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRequest implements Insertable {
    private User user;

    @org.jetbrains.annotations.Contract(pure = true)
    public UserRequest(User user) {
        this.user = user;
    }


    @Override
    @NotNull
    public PreparedStatement getInsertStatement() throws SQLException {
        String query = "INSERT INTO users(first_name,last_name,date_of_birth,email,password, salt) VALUES(?,?,?,?,?,?);";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setString(1, user.getFirstName());
        stmt.setString(2, user.getLastName());
        stmt.setDate(3, new Date(user.getDateOfBirth().getTime()));
        stmt.setString(4, user.getEmail());
        stmt.setString(5, user.getPassword());
        stmt.setString(6, user.getSalt());
        return stmt;
    }

    @Override
    public String description() {
        return "Insert new user into DB";
    }
}
