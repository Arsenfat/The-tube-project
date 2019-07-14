package com.tubeproject.view.connected.travel;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.algorithm.PathCalculator;
import com.tubeproject.algorithm.PathResponse;
import com.tubeproject.controller.Line;
import com.tubeproject.controller.StationWLine;
import com.tubeproject.controller.User;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.interfaces.Injectable;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.BurgerMenu;
import com.tubeproject.view.component.TravelViewer;
import com.tubeproject.view.component.WebButton;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class JourneyScreen extends Application implements Initializable, Injectable {

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
    private Label lbQuickestListLine;

    @FXML
    private Label lbLessConnectionListLine;

    @FXML
    private Label lbBegin;

    @FXML
    private Label lbEnd;

    @FXML
    private Pane travelViewer;

    @FXML
    private Pane pnlLessConnection;

    @FXML
    private Pane pnlQuickest;

    private List<Line> listLines = new ArrayList<>();

    private Map<String, Object> contextMap;
    private BurgerMenu burgerPane;
    @FXML
    private void handleButtonActionHomePage() {
        ContextMap.getContextMap().put("USER", null);
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    @FXML
    private void handleButtonActionGoBack() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.TRAVEL_SCREEN);
    }

    @Override
    public void injectMap(Map<String, Object> map) {
        contextMap = map;
        System.out.println(contextMap);
        burgerPane.checkUserLoggedIn((User) contextMap.get("USER"));
        PathResponse pathResponse = PathCalculator.calculatePath((StationWLine) contextMap.get("START_STATION"), (StationWLine) contextMap.get("END_STATION"));

        fillPage(pathResponse);
        TravelViewer quickest = new TravelViewer(267, 342, new Image(getClass().getResourceAsStream(Resources.Images.TUBE_MAP)));
        quickest.drawTravel(pathResponse.getQuickest());
        TravelViewer lessConnection = new TravelViewer(267, 342, new Image(getClass().getResourceAsStream(Resources.Images.TUBE_MAP)));
        lessConnection.drawTravel(pathResponse.getLessConnection());

        pnlQuickest.setOnMouseEntered((event) -> {
            changeTravelViewer(quickest);
        });

        pnlLessConnection.setOnMouseEntered((event) -> {
            changeTravelViewer(lessConnection);
        });

        changeTravelViewer(quickest);
    }

    private void changeTravelViewer(TravelViewer tv) {
        travelViewer.getChildren().removeAll(travelViewer.getChildren());
        travelViewer.getChildren().add(tv);
    }

    private void fillPage(PathResponse pathResponse) {
        lbBegin.setText(((StationWLine) contextMap.get("START_STATION")).getName());
        lbEnd.setText(((StationWLine) contextMap.get("END_STATION")).getName());

        initializeQuickestListLine(pathResponse.getQuickest());
        initializeLessConnectionListLine(pathResponse.getLessConnection());
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.JOURNEY_SCREEN);
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawer.setVisible(false);
        initializeImgView();
        webButtonPane.getChildren().add(new WebButton(this.getHostServices()));
        initializeBurger();
        initializeBackground();


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

    public void initializeQuickestListLine(List<StationWLine> list) {
        lbQuickestListLine.setText(list.stream().map((station) -> station.getLine().getName()).distinct().collect(Collectors.joining(", ")));
    }

    public void initializeLessConnectionListLine(List<StationWLine> list) {
        lbLessConnectionListLine.setText(list.stream().map((station) -> station.getLine().getName()).distinct().collect(Collectors.joining(", ")));
    }
}
