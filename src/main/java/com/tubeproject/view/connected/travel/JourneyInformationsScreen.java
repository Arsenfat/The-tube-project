package com.tubeproject.view.connected.travel;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.select.GetStationsFromLineRequest;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.LinePane;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class JourneyInformationsScreen extends Application implements Initializable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXButton facebookIcon;

    @FXML
    private JFXButton twitterIcon;

    @FXML
    private JFXButton instagramIcon;

    @FXML
    private JFXButton mailIcon;

    @FXML
    private JFXHamburger burger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private ScrollPane scrlPane;

    @FXML
    private VBox vbxLineContainer;

    @FXML
    private void handleButtonActionHomePage() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.JOURNEY_INFORMATIONS_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        scene.getStylesheets().add(StageManager.class.getResource(Resources.Stylesheets.TRANSPARENT_SCROLL_PANE).toExternalForm());
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawer.setVisible(false);
        initializeImgView();
        initializeBackground();
        initializeIcons();
        initializeBurger();
        try {
            DatabaseConnection.DatabaseOpen();
            Line bakerloo = new Line(1, "Bakerloo");
            GetStationsFromLineRequest getStationsFromLineRequest = new GetStationsFromLineRequest(bakerloo);
            bakerloo.setStations((List<Station>) new Select(getStationsFromLineRequest).select().get());
            for (int i = 0; i < 10; i++) {
                LinePane lp = new LinePane(bakerloo, String.format("%d lo", i), false);
                vbxLineContainer.getChildren().add(lp);
                vbxLineContainer.setPrefHeight(vbxLineContainer.getMaxHeight() + lp.getHeight());
            }

            VBox.setVgrow(vbxLineContainer, Priority.ALWAYS);
            VBox.setVgrow(scrlPane, Priority.ALWAYS);

            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
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

    private void initializeIcons() {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage bgImg = ImageUtils.loadBackgroundImage(Resources.Images.FACEBOOK, backgroundSize);
        facebookIcon.setBackground(new Background(bgImg));

        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        bgImg = ImageUtils.loadBackgroundImage(Resources.Images.TWITTER, backgroundSize);
        twitterIcon.setBackground(new Background(bgImg));


        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        bgImg = ImageUtils.loadBackgroundImage(Resources.Images.INSTAGRAM, backgroundSize);
        instagramIcon.setBackground(new Background(bgImg));

        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        bgImg = ImageUtils.loadBackgroundImage(Resources.Images.MAIL, backgroundSize);
        mailIcon.setBackground(new Background(bgImg));

    }

    public void initializeBurger() {
        //drawer.setSidePane(new BurgerMenu());
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
