package com.tubeproject.view.component;

import com.tubeproject.view.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;

public class BurgerMenu extends VBox {
    private Node view;
    private BurgerMenuController controller;

    public BurgerMenu() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Resources.Components.BURGER_MENU));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return controller = new BurgerMenuController();
            }
        });
        try {
            view = fxmlLoader.load();

        } catch (IOException ex) {
        }
        getChildren().add(view);
    }
}
