package com.tubeproject.view.component;

import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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

    @FXML
    private VBox vbxStations;

    @FXML
    private Pane parent;


    private double lblYPos;
    private Line line;
    private String direction;
    private boolean isLast;
    private boolean isExpanded;


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
        lblYPos = lblChangePlatform.getLayoutBounds().getMaxY();
        fillVBox();
        vbxStations.setVisible(false);
        this.isExpanded = false;
    }

    @FXML
    public void expandStations() {
        if (isExpanded) {
            vbxStations.setVisible(false);
            isExpanded = false;
        } else {
            vbxStations.setVisible(true);
            lblChangePlatform.setLayoutY(lblYPos + vbxStations.getHeight());
            parent.setMaxHeight(parent.getHeight() + lblYPos + vbxStations.getHeight());
            isExpanded = true;
        }
    }

    private void fillVBox() {
        for (Station station : line.getStations()) {
            vbxStations.getChildren().add(new Label(station.getName()));
        }
    }
}
