package com.tubeproject.view.component;

import com.tubeproject.controller.Line;
import com.tubeproject.view.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;

public class LinePane extends Pane {
    private Node view;
    private LinePaneController controller;

    public LinePane(Line line, String direction, boolean isLast) {
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
        controller.setLine(line);
        controller.setDirection(direction);
        controller.setLast(isLast);
        controller.init();
        getChildren().add(view);

    }
}
