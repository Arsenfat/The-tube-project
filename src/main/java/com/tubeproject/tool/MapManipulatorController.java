package com.tubeproject.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Select;
import com.tubeproject.model.requests.GetAllLinesWithStationsRequest;
import com.tubeproject.model.requests.GetAllStationsRequest;
import com.tubeproject.utils.FXMLUtils;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MapManipulatorController extends Application implements Initializable {


    private static List<LineMap> lineList;
    private StationMapPos currentMapPos = new StationMapPos(new Station());

    @FXML
    ComboBox<LineMap> lines;

    @FXML
    ListView<StationMapPos> stations;

    @FXML
    Button btnSave;

    @FXML
    ImageView imgView;

    @FXML
    Pane parentPane;

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

        imgView.setImage(new Image("/img/tube_map.gif"));
        double width = imgView.getFitWidth();
        double heigth = imgView.getFitHeight();
        Bounds b = imgView.getBoundsInParent();

        System.out.println(String.format("%f %f", b.getMaxX(), b.getMaxY()));
        imgView.setOnMouseClicked(event -> {
            currentMapPos.setX(event.getX());
            currentMapPos.setY(event.getY());
            for (Node node : parentPane.getChildren()) {
                if (node instanceof Circle) {
                    Circle c = (Circle) node;
                    if (c.getId().equals(currentMapPos.getNaptan())) {
                        c.setCenterY(event.getY() + 14);
                        c.setCenterX(event.getX() + 14);
                        break;
                    }
                }
            }
            System.out.println(String.format("%s (%f;%f)", currentMapPos.getName(), event.getX(), event.getY()));
            stations.refresh();
        });
        try {
            //stationList = loadStations();
            if (!load()) {
                lineList = loadLines().stream()//Map Line object to LineMap
                        .map(line -> new LineMap(line, line.getStations().stream()
                                .map(StationMapPos::new)//Map Station object to StationMapPos
                                .collect(Collectors.toList())))
                        .collect(Collectors.toList());
            }
        } catch (SQLException e) {
            System.out.println("PROBLEME ON EXIT");
            System.exit(2);
        }
        lines.setItems(FXCollections.observableArrayList(lineList));
        lines.valueProperty().addListener((observable, oldValue, newValue) -> {
            stations.setItems(FXCollections.observableArrayList(newValue.getStationTool()));
        });

        stations.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            currentMapPos = newValue;
        }));
        lineList.forEach((lineMap -> lineMap.getStationTool().forEach(this::drawCircle)));
    }

    public void drawCircle(StationMapPos s1) {
        Circle c = new Circle();
        c.setCenterX(s1.getX() + 14);
        c.setCenterY(s1.getY() + 14);
        c.setRadius(4.5);
        c.setId(s1.getNaptan());
        c.setMouseTransparent(true);
        parentPane.getChildren().add(c);
    }

    @FXML
    public void save() {
        try (FileOutputStream fO = new FileOutputStream(new File("src/main/resources/manip.json"))) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(lineList);
            fO.write(jsonString.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            System.out.println("ERROR: json processing error " + e);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: file not found " + e);
        } catch (IOException e) {
            System.out.println("ERROR: IO exception " + e);
        }

    }

    public boolean load() {
        try {
            //String jsonString = Files.readString(Paths.get("~/stationList.json"), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            lineList = mapper.readValue(getClass().getResourceAsStream("/manip.json").readAllBytes(), new TypeReference<List<LineMap>>() {
            });
            return true;
        } catch (JsonProcessingException e) {
            System.out.println("ERROR: json processing error " + e);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: file not found " + e);
        } catch (IOException e) {
            System.out.println("ERROR: IO exception " + e);
        }
        return false;
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
