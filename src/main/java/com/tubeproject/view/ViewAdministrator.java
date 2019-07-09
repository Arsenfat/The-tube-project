package com.tubeproject.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.model.ContextMap;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewAdministrator extends Application implements Initializable {

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
    private JFXDrawer drawer;

    @FXML
    private JFXHamburger burger;



    @FXML
    private void handleButtonActionHomePage() {
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
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.ADMINISTRATOR_SCREEN);

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
