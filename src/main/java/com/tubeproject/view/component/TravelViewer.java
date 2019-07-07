package com.tubeproject.view.component;

import com.tubeproject.tool.LineMap;
import com.tubeproject.tool.StationMapPos;
import com.tubeproject.view.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class TravelViewer extends Pane {

    private Node view;
    private TravelViewerController controller;

    public TravelViewer() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Resources.Components.TRAVEL_VIEWER));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return controller = new TravelViewerController();
            }
        });
        try {
            view = fxmlLoader.load();

        } catch (IOException ex) {
        }
        getChildren().add(view);
    }


    public void setHeight(double height) {
        controller.setMaxHeight(height);
    }

    public void setWidth(double width) {
        controller.setMaxWidth(width);
    }

    public void init(double width, double height, Image map) {
        controller.init(width, height, map);
    }

    public void reset() {
        controller.reset();
    }

    public void setBaseSize(double width, double height) {
        controller.setBaseSize(width, height);
    }

    public void drawTravel(List<LineMap> lineMapList) {
        for (LineMap lMap : lineMapList) {
            for (int i = 0; i < lMap.getStationTool().size() - 1; i++) {
                drawLine(lMap.getStationTool().get(i), lMap.getStationTool().get(i + 1));
            }
        }
    }

    public void drawLine(StationMapPos s1, StationMapPos s2) {
        controller.drawLine(s1, s2);
    }
}
