package com.tubeproject.core;

import com.tubeproject.view.ViewMainScreen;

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
        DatabaseConnection.DatabaseOpen();
        GetAllStationsRequest stations = new GetAllStationsRequest();
        Select s = new Select(stations);
        List<Station> stationList = (List<Station>) s.select().get();
        stationList.stream().forEach(System.out::println);
        DatabaseConnection.DatabaseClose();*/

        ViewMainScreen.startWindow();
    }

}
