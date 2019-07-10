package com.tubeproject.model.requests.select;

import com.tubeproject.controller.Station;
import com.tubeproject.model.builder.StationBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestUtils {
    public static Optional<List<Station>> buildListFromRS(ResultSet resultSet) {
        List<Station> stations = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String naptan = resultSet.getString("naptan");
                String name = resultSet.getString("name");
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");
                boolean wheelchair = resultSet.getBoolean("wheelchair");
                Station tmp = new StationBuilder()
                        .setNaptan(naptan)
                        .setName(name)
                        .setLatitude(latitude)
                        .setLongitude(longitude)
                        .setWheelchair(wheelchair)
                        .createStation();
                stations.add(tmp);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Optional.empty();
        }
        return Optional.of(stations);
    }

}
