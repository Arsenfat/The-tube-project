package com.tubeproject.view.component;

import com.tubeproject.controller.Line;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LinePaneController implements Initializable {
    @FXML
    private Label lblLine;

    @FXML
    private Label lblDirection;

    @FXML
    private Label lblStations;

    @FXML
    private Label lblChangePlatform;

    private Line line;
    private String direction;
    private boolean isLast;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public void init() {
        this.lblLine.setText(line.getName());
        this.lblStations.setText(String.format("%d stations", line.getStations().size()));
        this.lblDirection.setText(direction);
        if (isLast) {
            this.lblChangePlatform.setVisible(false);
        }
    }
}
