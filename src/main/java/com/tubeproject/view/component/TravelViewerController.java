package com.tubeproject.view.component;

import com.tubeproject.tool.StationMapPos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

public class TravelViewerController implements Initializable {

    @FXML
    public ImageView imgView;

    @FXML
    private Pane parent;

    private double height;
    private double width;
    private Image map;
    private double baseHeight;
    private double baseWidth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(parent.getWidth() + " " + parent.getHeight());

        parent.setStyle("-fx-background-color: blue;");
        imgView.setOnMouseClicked((mouseEvent -> {
            System.out.println(String.format("CLICK %f, %f", mouseEvent.getX(), mouseEvent.getY()));
        }));

    }

    public void init(double width, double height, Image map) {
        this.height = height;
        this.width = width;
        this.map = map;
        setMaxWidth(width);
        setMaxHeight(height);
        imgView.setFitWidth(width);
        imgView.setFitHeight(height);
        imgView.setImage(map);
    }

    public void reset() {
        init(width, height, map);
    }

    public void setBaseSize(double width, double height) {
        baseWidth = width;
        baseHeight = height;
    }

    public void setMaxHeight(double height) {
        parent.setMaxHeight(height);
    }

    public void setMaxWidth(double width) {
        parent.setMaxWidth(width);
    }

    public void setMap(Image map) {
        this.map = map;
        imgView.setImage(map);
    }

    private double computeRelativeHeight(double value) {
        return value * (height / baseHeight);
    }

    private double computeRelativeWidth(double value) {
        return value * (width / baseWidth);
    }

    public void drawLine(StationMapPos s1, StationMapPos s2) {
        double s1Width = computeRelativeWidth(s1.getX());
        double s1Height = computeRelativeHeight(s1.getY());
        double s2Width = computeRelativeWidth(s2.getX());
        double s2Height = computeRelativeHeight(s2.getY());
        System.out.println(String.format("pos %f, %f", parent.getLayoutX(), parent.getLayoutY()));
        System.out.println("s1 = " + s1);
        System.out.println(String.format("%f, %f", s1Width, s1Height));
        System.out.println("s2 = " + s2);
        System.out.println(String.format("%f, %f", s2Width, s2Height));
        Line line = new Line(s1Width, s1Height, s2Width, s2Height);
        line.setFill(null);
        line.setStroke(Color.RED);
        line.setStrokeWidth(3);
        parent.getChildren().add(line);
    }
}
