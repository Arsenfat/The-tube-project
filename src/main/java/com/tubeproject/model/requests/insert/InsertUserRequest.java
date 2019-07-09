package com.tubeproject.model.requests.insert;

import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Insertable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertUserRequest implements Insertable {
    private User user;

    public InsertUserRequest(User user) {
        this.user = user;
    }


    @Override
    @Description("Insert new user into Database")
    public PreparedStatement getInsertStatement() throws SQLException {
        String query = "INSERT INTO users(first_name,last_name,date_of_birth,email,password,salt,role) VALUES(?,?,?,?,?,?,1);";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setString(1, user.getFirstName());
        stmt.setString(2, user.getLastName());
        stmt.setString(3, user.getDateOfBirth().toString());
        stmt.setString(4, user.getEmail());
        stmt.setString(5, user.getPassword());
        stmt.setString(6, user.getSalt());
        return stmt;
    }
}
