package com.tubeproject.view.component;

import com.tubeproject.controller.Station;
import com.tubeproject.controller.StationWLine;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
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

    private List<StationWLine> stationList;
    private String direction;
    private boolean isLast;
    private boolean isExpanded;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public List<StationWLine> getStationlist() {
        return stationList;
    }

    public void setStationList(List<StationWLine> stationList) {
        this.stationList = stationList;
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
        this.lblLine.setText(stationList.get(0).getLine().getName());
        this.lblStations.setText(String.format("%d stations", stationList.size()));
        this.lblDirection.setText(direction);
        if (isLast) {
            this.lblChangePlatform.setVisible(false);
        }
        fillVBox();
        vbxStations.setVisible(true);
        this.isExpanded = true;
    }

    @FXML
    public void expandStations() {
        VBox vbxParent = (VBox) parent.getParent().getParent();
        LinePane parentLinePane = (LinePane) parent.getParent();
        double platformY = lblChangePlatform.getLayoutBounds().getMaxY() + lblDirection.getLayoutBounds().getMaxY() + lblStations.getLayoutBounds().getMaxY() + 22;
        if (isExpanded) {
            lblChangePlatform.setLayoutY(platformY);
            parent.setMaxHeight(parent.getHeight() + platformY + vbxStations.getLayoutBounds().getMaxY());
            vbxStations.setVisible(false);
            isExpanded = false;
            vbxParent.setMaxHeight(vbxParent.getMaxHeight() - parent.getMaxHeight());

        } else {
            vbxStations.setVisible(true);
            lblChangePlatform.setLayoutY(platformY + vbxStations.getHeight());
            parent.setMaxHeight(parent.getHeight() + platformY + vbxStations.getHeight());
            isExpanded = true;
            vbxParent.setMaxHeight(vbxParent.getMaxHeight() + parent.getMaxHeight());
        }
        vbxParent.setAlignment(vbxParent.getAlignment());
        parentLinePane.setMaxHeight(parent.getMaxHeight());
        System.out.println(parent.getHeight());
    }

    private void fillVBox() {
        for (Station station : stationList) {
            vbxStations.getChildren().add(new Label(station.getName()));
        }
    }
}
