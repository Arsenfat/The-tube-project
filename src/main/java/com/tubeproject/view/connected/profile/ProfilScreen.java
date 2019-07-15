package com.tubeproject.view.connected.profile;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.User;
import com.tubeproject.model.interfaces.Injectable;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.BurgerMenu;
import com.tubeproject.view.component.WebButton;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class ProfilScreen extends Application implements Initializable, Injectable {

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
    private Label lbFirstName;

    @FXML
    private Label lbLastName;

    @FXML
    private Label lbEmail;

    @FXML
    private Label lbBirth;

    private Map<String, Object> contextMap;
    private BurgerMenu burgerPane;

    @FXML
    private void handleButtonActionHomePage() {
        contextMap.put("USER", null);
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    @FXML
    private void handleButtonActionForgotPassword() {
        contextMap.put("USER", null);
        StageManager.changeStage(anchorPane, Resources.ViewFiles.CHANGE_PASSWORD_SCREEN);

    }

    @Override
    public void injectMap(Map<String, Object> map) {
        contextMap = map;
        initializeUser();
        burgerPane.checkUserLoggedIn((User) contextMap.get("USER"));
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.PROFIL_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawer.setVisible(false);
        initializeImgView();
        initializeBackground();
        webButtonPane.getChildren().add(new WebButton(this.getHostServices()));
        initializeBurger();
    }

    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent) node, nodes);
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

    public void initializeUser() {
        User user = (User) contextMap.get("USER");
        if (user != null) {
            lbFirstName.setText(user.getFirstName());
            lbLastName.setText(user.getLastName());
            lbEmail.setText(user.getEmail());
            lbBirth.setText(user.getDateOfBirth().toString());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not connected");
            alert.setHeaderText("Please log in");
            alert.setContentText("You must be connected to have access to this page.");
            alert.showAndWait();
            StageManager.changeStage(anchorPane, Resources.ViewFiles.LOGIN_SCREEN);
        }
    }
}
