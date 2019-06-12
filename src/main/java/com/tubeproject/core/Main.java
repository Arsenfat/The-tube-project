package com.tubeproject.core;

import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Insert;
import com.tubeproject.model.requests.UserRequest;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

public class Main {
    public static void main(String[] args){
        DatabaseConnection.DatabaseOpen();
        User user = new User("Martin",
                "Dupont",
                Date.valueOf(LocalDate.of(1992, Month.APRIL, 20)),
                "martin.dupont@gmail.com",
                "patrick",
                1);

        UserRequest uR = new UserRequest(user);
        Insert insert = new Insert(uR);
        insert.insert();
        DatabaseConnection.DatabaseClose();
    }

}
