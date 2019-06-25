package com.tubeproject.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.InputStream;

public class FXMLUtils {
    private FXMLUtils() {
        //do not instantiate
    }

    public static AnchorPane loadFXML(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        InputStream fxmlStream = FXMLUtils.class.getResourceAsStream(path);
        return loader.load(fxmlStream);
    }
}
