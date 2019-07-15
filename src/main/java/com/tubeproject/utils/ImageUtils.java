package com.tubeproject.utils;

import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import java.io.InputStream;

public class ImageUtils {

    public static Image loadImage(String resource) {
        InputStream stream = ImageUtils.class.getResourceAsStream(resource);
        return new Image(stream);
    }

    public static BackgroundImage loadBackgroundImage(String resource, BackgroundSize size) {
        Image img = loadImage(resource);
        return new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size);

    }
}
