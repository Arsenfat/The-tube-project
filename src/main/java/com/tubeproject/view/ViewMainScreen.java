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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewMainScreen extends Application implements Initializable {

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
    private void handleButtonActionLogin(ActionEvent event) {
        AnchorPane loginPage;
        try {
            loginPage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.LOGIN_SCREEN));

        } catch (IOException e) {
            System.out.println("Warning unandled exeption.");
            return;
        }
        Scene loginScene = new Scene(loginPage);
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.setScene(loginScene);
        loginStage.show();
    }

    @FXML
    private void handleButtonActionSignUp(ActionEvent event) {
        AnchorPane signUpPage;
        try {
            signUpPage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.SIGN_UP_SCREEN));

        } catch (IOException e) {
            System.out.println("Warning unandled exeption.");
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
        AnchorPane anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.MAIN_SCREEN);

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

    private void initializeBackground() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.BACKGROUND);
        Image img = new Image(stream);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        anchorPane.setBackground(new Background(backgroundImage));
    }


    private void initializeImgView() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.LOGO1);
        Image img = new Image(stream);
        this.imgView.setImage(img);
    }

    private void initializeIcons() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.FACEBOOK);
        Image img = new Image(stream);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        facebookIcon.setBackground(new Background(backgroundImage));

        stream = getClass().getResourceAsStream(Resources.Images.TWITTER);
        img = new Image(stream);
        backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        twitterIcon.setBackground(new Background(backgroundImage));

        stream = getClass().getResourceAsStream(Resources.Images.INSTAGRAM);
        img = new Image(stream);
        backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        instagramIcon.setBackground(new Background(backgroundImage));

        stream = getClass().getResourceAsStream(Resources.Images.MAIL);
        img = new Image(stream);
        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        mailIcon.setBackground(new Background(backgroundImage));

    }
}
