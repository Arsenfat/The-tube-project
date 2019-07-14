package com.tubeproject.view.administration;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.User;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.interfaces.Injectable;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.select.ComputeNumberTravelsRequest;
import com.tubeproject.model.requests.select.ComputeUserQuantityRequest;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.BurgerMenu;
import com.tubeproject.view.component.WebButton;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticsScreen extends Application implements Initializable, Injectable {

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
    private Label lblNbUsers;

    @FXML
    private Label lblNbTravels;

    @FXML
    private Label lblTotalTimeTransport;

    private BurgerMenu burgerPane;

    private Map<String, Object> contextMap;

    @FXML
    private void handleButtonActionHomePage() {
        ContextMap.getContextMap().put("USER", null);
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    @FXML
    private void handleButtonActionGoBack() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.ADMINISTRATOR_SCREEN);
    }

    @Override
    public void injectMap(Map<String, Object> map) {
        contextMap = map;
        burgerPane.checkUserLoggedIn((User) contextMap.get("USER"));
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.STATISTICS_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawer.setVisible(false);
        initializeImgView();
        initializeBackground();
        webButtonPane.getChildren().add(new WebButton(this.getHostServices()));
        initializeBurger();
        fillData();
    }

    public void fillData() {
        int nbUsers = 0, nbTravels = 0, nbHours = 0;
        try {
            DatabaseConnection.DatabaseOpen();
            ComputeUserQuantityRequest computeUserQuantityRequest = new ComputeUserQuantityRequest();
            Select s = new Select(computeUserQuantityRequest);
            nbUsers = (Integer) s.select().get();
            ComputeNumberTravelsRequest computeNumberTravelsRequest = new ComputeNumberTravelsRequest();
            s = new Select(computeNumberTravelsRequest);
            nbTravels = (Integer) s.select().get();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't reach server");
            alert.setHeaderText("Can't reach the server");
            alert.showAndWait();
        }
        lblNbUsers.setText(String.format("%d", nbUsers));
        lblNbTravels.setText(String.format("%d", nbTravels));

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
