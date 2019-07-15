package com.tubeproject.view.component;

import com.tubeproject.controller.StationWLine;
import com.tubeproject.view.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class LinePane extends Pane {
    private Node view;
    private LinePaneController controller;

    public LinePane(List<StationWLine> line, String direction, boolean isLast) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Resources.Components.LINE_PANE));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return controller = new LinePaneController();
            }
        });
        try {
            view = fxmlLoader.load();

        } catch (IOException ex) {
            System.out.println(ex);
        }
        controller.setStationList(line);
        controller.setDirection(direction);
        controller.setLast(isLast);
        controller.init();
        getChildren().add(view);

    }
}
