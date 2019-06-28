package com.tubeproject.model.requests;

import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.annotation.Description;
import com.tubeproject.model.interfaces.Selectable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class LoginRequest implements Selectable {
    private String email;


    public LoginRequest(String email) {
        this.email = email;
    }

    @Override
    @Description("Querying users from database")
    public PreparedStatement getSelectQuery() throws SQLException {
        String query = "SELECT first_name, last_name, date_of_birth, email, password, salt, home, work, role FROM users WHERE email=?";
        PreparedStatement stmt = DatabaseConnection.prepareStmt(query);
        stmt.setString(1, email);
        return stmt;
    }

    @Override
    public Optional<User> buildFromResult(ResultSet resultSet) {
        String firstName;
        String lastName;
        Date date;
        String email;
        String password;
        String salt;
        int home, work, role;
        try {
            if (resultSet.next()) {
                firstName = resultSet.getString("first_name");
                lastName = resultSet.getString("last_name");
                date = resultSet.getDate("date_of_birth");
                email = resultSet.getString("email");
                password = resultSet.getString("password");
                salt = resultSet.getString("salt");
                home = resultSet.getInt("home");
                work = resultSet.getInt("work");
                role = resultSet.getInt("role");
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Optional.empty();
        }

        User u = new User(firstName, lastName, date, email, password, role);
        u.setSalt(salt);


        return Optional.of(u);
    }

}
