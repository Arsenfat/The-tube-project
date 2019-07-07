package com.tubeproject.view.component;

import com.tubeproject.tool.LineMap;
import com.tubeproject.tool.StationMapPos;
import com.tubeproject.view.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class TravelViewer extends Pane {

    private Node view;
    private TravelViewerController controller;

    public TravelViewer() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Resources.Components.TRAVEL_VIEWER));
        fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return controller = new TravelViewerController();
            }
        });
        try {
            view = fxmlLoader.load();

        } catch (IOException ex) {
        }
        getChildren().add(view);
    }


    public void setHeight(double height) {
        controller.setMaxHeight(height);
    }

    public void setWidth(double width) {
        controller.setMaxWidth(width);
    }

    public void init(double width, double height, Image map) {
        controller.init(width, height, map);
    }

    public void reset() {
        controller.reset();
    }

    public void drawTravel(List<LineMap> lineMapList) {
        double minX = 0;
        double maxX = 0;
        double minY = 0;
        double maxY = 0;
        for (LineMap lMap : lineMapList) {
            for (int i = 0; i < lMap.getStationTool().size() - 1; i++) {
                StationMapPos s1 = lMap.getStationTool().get(i);
                StationMapPos s2 = lMap.getStationTool().get(i + 1);
                drawLine(s1, s2);
                drawCircle(s1);
                minX = threeMin(minX, s1.getX(), s2.getX());
                maxX = threeMax(maxX, s1.getX(), s2.getX());
                minY = threeMin(minY, s1.getY(), s2.getY());
                maxY = threeMax(maxY, s1.getY(), s2.getY());
            }
            drawCircle(lMap.getStationTool().get(lMap.getStationTool().size() - 1));
        }

        //TODO LES MATHS DU ZOOM SI C'EST POSSIBLE
    }

    private double threeMin(double v1, double v2, double v3) {
        double min;
        if (v1 <= v2 && v1 <= v3) {
            min = v1;
        } else if (v2 <= v3 && v2 <= v1) {
            min = v2;
        } else {
            min = v3;
        }

        return min;
    }

    private double threeMax(double v1, double v2, double v3) {
        double max;
        if (v1 >= v2 && v1 <= v3) {
            max = v1;
        } else if (v2 >= v3 && v2 >= v1) {
            max = v2;
        } else {
            max = v3;
        }

        return max;
    }

    public void drawCircle(StationMapPos s1) {
        controller.drawCircle(s1);
    }

    public void drawLine(StationMapPos s1, StationMapPos s2) {
        controller.drawLine(s1, s2);
    }
}
