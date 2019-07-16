package com.tubeproject.view.connected.travel;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.*;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.interfaces.Injectable;
import com.tubeproject.model.requests.Insert;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.insert.InsertStationsTravelRequest;
import com.tubeproject.model.requests.insert.InsertTravelRequest;
import com.tubeproject.model.requests.select.GetFaresRequest;
import com.tubeproject.model.requests.select.GetZoneFromStationRequest;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.BurgerMenu;
import com.tubeproject.view.component.WebButton;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JourneyInformationsScreen implements Initializable, Injectable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane webButtonPane;

    @FXML
    private JFXHamburger burger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Label lblFrom;

    @FXML
    private Label lblTo;

    @FXML
    private Label wheelchair;

    private Map<String, Object> contextMap;
    private BurgerMenu burgerPane;

    @FXML
    private Label lblHour;

    @FXML
    private Label lblOyster;

    @FXML
    private Label lblTicket;

    @FXML
    private ScrollPane scrlPane;

    @FXML
    private VBox vbxLineContainer;

    private static ExecutorService threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 300, TimeUnit.SECONDS, new PriorityBlockingQueue<>());
    }

    @FXML
    private void handleButtonActionHomePage() {
        ContextMap.getContextMap().put("USER", null);
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    @FXML
    private void handleButtonActionGoBack() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.JOURNEY_SCREEN, Resources.Stylesheets.MENU);
    }

    @Override
    public void injectMap(Map<String, Object> map) {
        contextMap = map;
        User user = (User) contextMap.get("USER");
        burgerPane.checkUserLoggedIn(user);
        webButtonPane.getChildren().add(new WebButton((HostServices) contextMap.get("HOSTED")));
        List<StationWLine> travel = (List<StationWLine>) contextMap.get("TRAVEL");
        fillLinePane(travel);
        lblHour.setText((String) contextMap.get("TIME"));
        StationWLine start = (StationWLine) contextMap.get("START_STATION");
        StationWLine end = (StationWLine) contextMap.get("END_STATION");
        lblFrom.setText(start.getName());
        lblTo.setText(end.getName());
        fillFares(start, end);
        threadPoolExecutor.submit(() -> insertTravel(user, start, end, travel));
    }

    private void insertTravel(User user, Station start, Station end, List<StationWLine> travel) {
        try {
            DatabaseConnection.DatabaseOpen();
            InsertTravelRequest insertTravelRequest = new InsertTravelRequest(user, start, end);
            Insert insert = new Insert(insertTravelRequest);
            insert.insert();
            int id = insert.getId();
            if (id > -1) {
                travel
                        .stream()
                        .map(stationWLine -> new InsertStationsTravelRequest(id, stationWLine))
                        .map(Insert::new)
                        .forEach(Insert::insert);
            }

            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawer.setVisible(false);
        initializeImgView();
        initializeBackground();
        initializeBurger();

        VBox.setVgrow(vbxLineContainer, Priority.ALWAYS);
        VBox.setVgrow(scrlPane, Priority.ALWAYS);
        scrlPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        vbxLineContainer.setSpacing(10);
        vbxLineContainer.setPadding(new Insets(10, 0, 10, 10));
        vbxLineContainer.setFillWidth(true);
    }

    private void fillFares(StationWLine start, StationWLine end) {
        List<Zone> startZones = loadZones(start);
        List<Zone> endZones = loadZones(end);
        List<Fare> fares = loadFares();
        int startZone_filter = startZones.stream().min(Zone::compareTo).orElse(new Zone("", 1)).getId();
        int endZone_filter = endZones.stream().min(Zone::compareTo).orElse(new Zone("", 1)).getId();
        int startZoneCorrection = Integer.min(startZone_filter, endZone_filter);
        int endZoneCorrection = Integer.max(startZone_filter, endZone_filter);

        int startZone = startZoneCorrection;
        int endZone = calculerFin(startZoneCorrection, endZoneCorrection);

        List<Fare> filteredFares = fares.stream().filter(fare -> fare.getDepartingZone().getId() == startZone && fare.getArrivingZone().getId() == endZone).collect(Collectors.toList());
        Fare ticketAdult = filteredFares.stream().filter((fare) -> fare.getType().equals(Fare.Type.ADULT)).findFirst().orElse(fares.get(fares.size() - 1));
        Fare oysterPeak = filteredFares.stream().filter((fare) -> fare.getType().equals(Fare.Type.OYSTER_PEAK)).findFirst().orElse(fares.get(fares.size() - 1));
        lblOyster.setText(String.format("£%.02f", oysterPeak.getPrice()));
        lblTicket.setText(String.format("£%.02f", ticketAdult.getPrice()));

    }

    int calculerFin(int i1, int i2) {
        if (i1 > 1 || i2 > 6)
            return 6;
        return i2;
    }

    private List<Fare> loadFares() {
        List<Fare> fares = new ArrayList<>();
        try {
            DatabaseConnection.DatabaseOpen();
            GetFaresRequest getFaresRequest = new GetFaresRequest();
            Select s = new Select(getFaresRequest);
            fares = (List<Fare>) s.select().get();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return fares;
    }

    private List<Zone> loadZones(StationWLine station) {
        List<Zone> zones = new ArrayList<>();
        try {
            DatabaseConnection.DatabaseOpen();
            GetZoneFromStationRequest getZoneFromStationRequest = new GetZoneFromStationRequest(station);
            Select s = new Select(getZoneFromStationRequest);
            zones = (List<Zone>) s.select().get();

            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return zones;
    }

    private void fillLinePane(List<StationWLine> list) {
        Map<Line, List<StationWLine>> map = list.stream().collect(Collectors.groupingBy(StationWLine::getLine));
        Font point23 = new Font("System Bold", 23.0);
        Font point15 = new Font(15.0);

        Line current = new Line();
        boolean wheelchairFriendly = list.get(0).isWheelchair();
        for (int i = 0; i < list.size(); i++) {
            StationWLine station = list.get(i);
            if (!current.equals(station.getLine())) {
                if (i > 0 && i < list.size() - 1) {
                    Label lblPlatform = new Label("Change platform");
                    lblPlatform.setFont(point23);
                    vbxLineContainer.getChildren().add(lblPlatform);
                    wheelchairFriendly &= station.isWheelchair();
                }
                current = station.getLine();
                Label line = new Label(capitalize(current.getName()));
                line.setFont(point23);
                vbxLineContainer.getChildren().add(line);
            }

            Label lblStation = new Label(station.getName());
            lblStation.setFont(point15);
            vbxLineContainer.getChildren().add(lblStation);

        }

        if (wheelchairFriendly) {
            wheelchair.setText("Wheelchair friendly");
        } else {
            wheelchair.setText("Non-wheelchair friendly");
        }

    }

    private String capitalize(String s) {
        return String.format("%s%s", s.substring(0, 1).toUpperCase(), s.substring(1).toLowerCase());

    }

    private void initializeBackground() {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        BackgroundImage bgImg = ImageUtils.loadBackgroundImage(Resources.Images.BACKGROUND, backgroundSize);
        anchorPane.setBackground(new Background(bgImg));
    }


    private void initializeImgView() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.LOGO1);
        Image img = new Image(stream);
        this.imgView.setImage(img);
    }


    public void initializeBurger() {
        burgerPane = new BurgerMenu();
        drawer.setSidePane(burgerPane);
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(burger);
        transition.setRate(-1);
        burger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (drawer.isShown()) {
                drawer.close();
                drawer.setVisible(false);
            } else {
                drawer.setVisible(true);
                drawer.open();
            }
        });
    }
}
