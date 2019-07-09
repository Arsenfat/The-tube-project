package com.tubeproject.view;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.Station;
import com.tubeproject.controller.Zone;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Select;
import com.tubeproject.model.requests.GetAllStations;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class EditFaresScreen extends Application implements Initializable {

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
    private void handleButtonActionHomePage() {
        System.out.println("you've clicked");
        AnchorPane homePage;
        try {
            homePage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.MAIN_SCREEN));

        } catch (IOException e) {
            System.out.println("Warning unandled exeption.");
            return;
        }
        Scene homeScene = new Scene(homePage);
        Stage homeStage = (Stage) anchorPane.getScene().getWindow();
        homeStage.setScene(homeScene);
        homeStage.show();
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.EDIT_FARES_SCREEN);

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
        initializeIcons();
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
        try {
            VBox vbox = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.VBOX));
            drawer.setSidePane(vbox);
            for (Node node : vbox.getChildren()) {
                if (node.getId() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        switch (node.getId()) {
                            case "btnHistory":
                                AnchorPane historyPage;
                                try {
                                    historyPage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.HISTORY_SCREEN));

                                } catch (IOException ex) {
                                    System.out.println("Warning unandled exeption.");
                                    return;
                                }
                                Scene homeScene = new Scene(historyPage);
                                Stage homeStage = (Stage) anchorPane.getScene().getWindow();
                                homeStage.setScene(homeScene);
                                homeScene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());
                                homeStage.show();
                                break;
                            case "btnAdministration":
                                AnchorPane administrationPage;
                                try {
                                    administrationPage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.ADMINISTRATOR_SCREEN));

                                } catch (IOException ex) {
                                    System.out.println("Warning unandled exeption.");
                                    return;
                                }
                                homeScene = new Scene(administrationPage);
                                homeStage = (Stage) anchorPane.getScene().getWindow();
                                homeStage.setScene(homeScene);
                                homeScene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());
                                homeStage.show();
                                break;
                            case "btnLogOut":
                                ContextMap.getContextMap().put("USER", null);
                                AnchorPane homePage;
                                try {
                                    homePage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.MAIN_SCREEN));

                                } catch (IOException ex) {
                                    System.out.println("Warning unandled exeption.");
                                    return;
                                }
                                homeScene = new Scene(homePage);
                                homeStage = (Stage) anchorPane.getScene().getWindow();
                                homeStage.setScene(homeScene);
                                homeStage.show();
                                break;
                            case "btnProfile":
                                AnchorPane profilPage;
                                try {
                                    profilPage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.PROFIL_SCREEN));

                                } catch (IOException ex) {
                                    System.out.println("Warning unandled exeption.");
                                    return;
                                }
                                homeScene = new Scene(profilPage);
                                homeStage = (Stage) anchorPane.getScene().getWindow();
                                homeStage.setScene(homeScene);
                                homeScene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());
                                homeStage.show();
                                break;
                        }
                    });
                }
            }

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
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
