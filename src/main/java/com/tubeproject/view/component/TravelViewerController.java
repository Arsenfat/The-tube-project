package com.tubeproject.view.component;

import com.tubeproject.tool.StationMapPos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    private static final double BASE_HEIGHT = 668.125;
    private static final double BASE_WIDTH = 1000;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parent.setStyle("-fx-background-color: blue;");
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
        imgView.setPreserveRatio(false);
    }

    public void reset() {
        init(width, height, map);
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
        return value * (height / BASE_HEIGHT);
    }

    private double computeRelativeWidth(double value) {
        return value * (width / BASE_WIDTH);
    }

    public void drawCircle(StationMapPos s1) {
        double s1X = computeRelativeWidth(s1.getX());
        double s1Y = computeRelativeHeight(s1.getY());
        Circle outerBlack = new Circle();
        outerBlack.setCenterX(s1X);
        outerBlack.setCenterY(s1Y);
        outerBlack.setRadius(3);
        outerBlack.setFill(Color.WHITE);
        outerBlack.setStroke(Color.BLACK);
        outerBlack.setStrokeWidth(2);
        parent.getChildren().add(outerBlack);
    }

    public void drawLine(StationMapPos s1, StationMapPos s2) {
        double s1Width = computeRelativeWidth(s1.getX());
        double s1Height = computeRelativeHeight(s1.getY());
        double s2Width = computeRelativeWidth(s2.getX());
        double s2Height = computeRelativeHeight(s2.getY());
        Line line = new Line(s1Width, s1Height, s2Width, s2Height);
        line.setFill(Color.RED);
        line.setStroke(Color.RED);
        line.setStrokeWidth(4);
        parent.getChildren().add(line);
    }

}
