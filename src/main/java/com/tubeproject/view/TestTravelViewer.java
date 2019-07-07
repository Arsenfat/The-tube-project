package com.tubeproject.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tubeproject.controller.Line;
import com.tubeproject.tool.LineMap;
import com.tubeproject.tool.StationMapPos;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.view.component.TravelViewer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TestTravelViewer extends Application implements Initializable {

    @FXML
    TravelViewer tV;

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane root = FXMLUtils.loadFXML("/view/TestTravelViewer.fxml");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(Resources.Images.TUBE_MAP);
        tV.init(500, 400, img);
        List<LineMap> ll = load().stream().filter((lMap) -> lMap.getId() == 1).collect(Collectors.toList());
        List<StationMapPos> t = List.of(getTmp(ll.get(0).getStationTool(), "Edgware Road"), getTmp(ll.get(0).getStationTool(), "Marylebone"), getTmp(ll.get(0).getStationTool(), "Baker Street"));
        LineMap lM = new LineMap(new Line(0, "wllah"), t);
        tV.drawTravel(List.of(lM));
    }

    public StationMapPos getTmp(List<StationMapPos> ll, String name) {
        return ll.stream().filter((stationMapPos -> stationMapPos.getName().contains(name))).collect(Collectors.toList()).get(0);
    }

    public List<LineMap> load() {
        try {

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(getClass().getResourceAsStream("/manip.json").readAllBytes(), new TypeReference<List<LineMap>>() {
            });
        } catch (JsonProcessingException e) {
            System.out.println("ERROR: json processing error " + e);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: file not found " + e);
        } catch (IOException e) {
            System.out.println("ERROR: IO exception " + e);
        }
        return null;
    }
}
