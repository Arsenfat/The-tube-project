package com.tubeproject.view.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tubeproject.controller.Line;
import com.tubeproject.controller.StationWLine;
import com.tubeproject.tool.LineMap;
import com.tubeproject.tool.StationMapPos;
import com.tubeproject.view.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravelViewer extends Pane {

    private Node view;
    private TravelViewerController controller;

    private static List<LineMap> allList;

    public TravelViewer(double width, double height, Image map) {
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
        controller.init(width, height, map);
        allList = load();
    }


    public void setHeight(double height) {
        controller.setMaxHeight(height);
    }

    public void setWidth(double width) {
        controller.setMaxWidth(width);
    }

    public void reset() {
        controller.reset();
    }

    public void drawTravel(List<StationWLine> stationWLineList) {
        List<LineMap> lineMapList = convertStationWline(stationWLineList, allList);

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

    private List<LineMap> convertStationWline(List<StationWLine> stationWLineList, final List<LineMap> lineMapList) {
        Map<Line, List<StationWLine>> map = new HashMap<>();

        for (StationWLine stationWLine : stationWLineList) {
            List<StationWLine> lineStationList = map.computeIfAbsent(stationWLine.getLine(), (key) -> new ArrayList<>());
            lineStationList.add(stationWLine);
        }

        List<LineMap> listToReturn = new ArrayList<>();

        for (Map.Entry<Line, List<StationWLine>> entry : map.entrySet()) {
            Line line = entry.getKey();

            List<StationMapPos> mapPos = new ArrayList<>();
            for (StationWLine station : entry.getValue()) {
                for (StationMapPos stationMapPos : lineMapList.stream().filter(tmpLine -> tmpLine.getId() == line.getId()).findFirst().orElse(new LineMap()).getStationTool()) {
                    if (station.getNaptan().equalsIgnoreCase(stationMapPos.getNaptan())) {
                        mapPos.add(stationMapPos);
                        break;
                    }
                }
            }

            listToReturn.add(new LineMap(line, mapPos));
        }


        return listToReturn;
    }

    private List<LineMap> load() {
        try {

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(getClass().getResourceAsStream("/manip.json").readAllBytes(), new TypeReference<List<LineMap>>() {
            });
        } catch (JsonProcessingException e) {
            System.out.println("ERROR: json processing error " + e);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: file not found " + e);
        } catch (IOException e) {
            System.out.println("ERROR: IO exception " + e);
        }
        return null;
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
