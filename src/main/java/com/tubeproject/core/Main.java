package com.tubeproject.core;

//Views

import com.tubeproject.view.*;
//Model
import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Select;
import com.tubeproject.model.requests.GetAllStations;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        /*DatabaseConnection.DatabaseOpen();
        User user = new User("Sophie",
            "Pages",
            Date.valueOf(LocalDate.of(1992, Month.MAY, 23)),
            "sophie.pages@gmail.com",
            "guillaume",
            3);
        user.crypt();*/

        ViewMainScreen.startWindow();
    }

}
