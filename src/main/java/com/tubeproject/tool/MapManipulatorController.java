package com.tubeproject.tool;

import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Select;
import com.tubeproject.model.requests.GetAllLinesWithStationsRequest;
import com.tubeproject.model.requests.GetAllStationsRequest;
import com.tubeproject.utils.FXMLUtils;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MapManipulatorController extends Application implements Initializable {

    private static List<Station> stationList;
    private static List<Line> lineList;

    @FXML
    ComboBox<Line> lines;

    @FXML
    ListView<Station> stations;

    @FXML
    Button btnSave;

    @FXML
    ImageView imgView;

    public static void launchWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = FXMLUtils.loadFXML("/view/MapManipScreen.fxml");

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double width = imgView.getFitWidth();
        double heigth = imgView.getFitHeight();
        imgView.setImage(new Image("/img/tube_map.gif"));
        imgView.setOnMouseClicked(event -> {
            System.out.println(String.format("(%f;%f)", event.getX(), event.getY()));
        });
        try {
            stationList = loadStations();
            lineList = loadLines();
        } catch (SQLException e) {
            System.out.println("PROBLEME ON EXIT");
            System.exit(2);
        }

        for (Line line : lineList) {
            System.out.println("###########################################################");
            System.out.println("line.getName() = " + line.getName());
            for (Station station : line.getStations()) {
                System.out.println("station.getName() = " + station.getName());
            }
        }
    }

    public List<Station> loadStations() throws SQLException {
        DatabaseConnection.DatabaseOpen();
        GetAllStationsRequest stations = new GetAllStationsRequest();
        Select s = new Select(stations);
        Optional<List<Station>> ret = (Optional<List<Station>>) s.select();
        if (ret.isEmpty()) {
            System.exit(1);
        }
        DatabaseConnection.DatabaseClose();
        return ret.get();
    }

    public List<Line> loadLines() throws SQLException {
        DatabaseConnection.DatabaseOpen();
        GetAllLinesWithStationsRequest lines = new GetAllLinesWithStationsRequest();
        Select s = new Select(lines);
        Optional<List<Line>> ret = (Optional<List<Line>>) s.select();
        if (ret.isEmpty()) {
            System.exit(1);
        }
        DatabaseConnection.DatabaseClose();
        return ret.get();
    }
}
