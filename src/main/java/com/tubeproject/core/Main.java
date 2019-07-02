package com.tubeproject.core;

import com.tubeproject.view.LoginScreen;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        /*DatabaseConnection.DatabaseOpen();
        User user = new User("Sophie",
            "Pages",
            Date.valueOf(LocalDate.of(1992, Month.MAY, 23)),
            "sophie.pages@gmail.com",
            "guillaume",
            3);
        user.crypt();
        UserRequest uR = new UserRequest(user);
        Insert insert = new Insert(uR);
        insert.insert();
        DatabaseConnection.DatabaseClose();*/

        LoginScreen.startWindow();
    }

}
