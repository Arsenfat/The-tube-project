package com.tubeproject.view.connected.profile;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.Station;
import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.interfaces.Injectable;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.select.GetLastThreeTravelsRequest;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.BurgerMenu;
import com.tubeproject.view.component.WebButton;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class HistoryScreen implements Initializable, Injectable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXHamburger burger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Pane webButtonPane;

    @FXML
    private Pane histo1;

    @FXML
    private Pane histo2;

    @FXML
    private Pane histo3;

    @FXML
    private Label from1;

    @FXML
    private Label to1;

    @FXML
    private Label from2;

    @FXML
    private Label to2;

    @FXML
    private Label from3;

    @FXML
    private Label to3;

    @FXML
    private javafx.scene.shape.Line line1;

    @FXML
    private javafx.scene.shape.Line line2;

    private Map<String, Object> contextMap;

    @Override
    public void injectMap(Map<String, Object> map) {
        contextMap = map;
        webButtonPane.getChildren().add(new WebButton((HostServices) contextMap.get("HOSTED")));
        fillHistory((User) contextMap.get("USER"));
    }

    private void fillHistory(User user) {
        Map<String, List<Station>> travelList = new HashMap<>();
        try {
            DatabaseConnection.DatabaseOpen();
            GetLastThreeTravelsRequest getLastThreeTravelsRequest = new GetLastThreeTravelsRequest(user);
            Select s = new Select(getLastThreeTravelsRequest);
            travelList = (Map<String, List<Station>>) s.select().get();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
        }
        for (Map.Entry<String, List<Station>> entry : travelList.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(histo1.getId())) {
                from1.setText(entry.getValue().get(0).getName());
                to1.setText(entry.getValue().get(1).getName());
                histo1.setVisible(true);
            } else if (entry.getKey().equalsIgnoreCase(histo2.getId())) {
                from2.setText(entry.getValue().get(0).getName());
                to2.setText(entry.getValue().get(1).getName());
                line1.setVisible(true);
                histo2.setVisible(true);
            } else if (entry.getKey().equalsIgnoreCase(histo3.getId())) {
                from3.setText(entry.getValue().get(0).getName());
                to3.setText(entry.getValue().get(1).getName());
                line2.setVisible(true);
                histo3.setVisible(true);
            }
        }
    }


    @FXML
    private void handleButtonActionHomePage() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawer.setVisible(false);
        initializeImgView();
        initializeBackground();
        initializeBurger();
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
        drawer.setSidePane(new BurgerMenu());
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

