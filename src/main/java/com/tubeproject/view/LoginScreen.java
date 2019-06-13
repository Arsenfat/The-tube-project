package com.tubeproject.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.tubeproject.utils.FXMLUtils;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreen extends Application implements Initializable {

    @FXML
    private ImageView imgView;

    @FXML
    private ImageView imgViewUser;

    @FXML
    private ImageView imgViewPassword;

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


    @FXML
    private void handleButtonActionSignUp(ActionEvent event) {
        AnchorPane signUpPage;
        try {
            signUpPage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.SIGN_UP_SCREEN));

        } catch (IOException e) {
            System.out.println("Warning unandle exeption.");
            return;
        }
        Scene signUpScene = new Scene(signUpPage);
        Stage signUpStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        signUpStage.setScene(signUpScene);
        signUpStage.show();
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.LOGIN_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeImgView();
        initializeBackground();
        initializeIcons();
    }

    private void initializeImgView() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.LOGO1);
        Image img = new Image(stream);
        imgView.setImage(img);
        stream = getClass().getResourceAsStream(Resources.Images.PASSWORD);
        img = new Image(stream);
        imgViewPassword.setImage(img);
        stream = getClass().getResourceAsStream(Resources.Images.USERNAME);
        img = new Image(stream);
        imgViewUser.setImage(img);
    }

    private void initializeBackground() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.BACKGROUND);
        Image img = new Image(stream);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        anchorPane.setBackground(new Background(backgroundImage));
    }

    private void initializeIcons() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.FACEBOOK);
        Image img = new Image(stream);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        facebookIcon.setBackground(new Background(backgroundImage));

        stream = getClass().getResourceAsStream(Resources.Images.TWITTER);
        img = new Image(stream);
        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        twitterIcon.setBackground(new Background(backgroundImage));

        stream = getClass().getResourceAsStream(Resources.Images.INSTAGRAM);
        img = new Image(stream);
        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        instagramIcon.setBackground(new Background(backgroundImage));

        stream = getClass().getResourceAsStream(Resources.Images.MAIL);
        img = new Image(stream);
        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        mailIcon.setBackground(new Background(backgroundImage));

    }
}
