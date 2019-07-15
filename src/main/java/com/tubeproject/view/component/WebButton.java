package com.tubeproject.view.component;

import com.tubeproject.view.Resources;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;

public class WebButton extends VBox {
    private Node view;
    private WebButtonController controller;

    public WebButton(HostServices hostServices) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Resources.Components.WEB_BUTTON));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return controller = new WebButtonController();
            }
        });
        try {
            view = fxmlLoader.load();

        } catch (IOException ex) {
            System.out.println(ex);
        }
        controller.setHostServices(hostServices);
        getChildren().add(view);
    }
}
