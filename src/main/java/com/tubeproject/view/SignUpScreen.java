package com.tubeproject.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

public class SignUpScreen extends Application implements Initializable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXButton facebookIcon;

    @FXML
    private JFXButton twitterIcon;

    @FXML
    private JFXButton instagramIcon;

    @FXML
    private JFXButton mailIcon;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtMail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private Label lbConnectToServer;

    @FXML
    private Label txtLabel;

    @FXML
    private DatePicker datePicker;


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
        AnchorPane anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.SIGN_UP_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.SIGN_UP_SCREEN).toExternalForm());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeImgView();
        initializeBackground();
        initializeIcons();
        checkTime();
    }

    @FXML
    private void handleButtonActionTrySignUp(ActionEvent event) {
        tryingToConnectToServer();
        checkFields();
        //TODO CHECK IF RIGHT MAIL
    }

    private void initializeImgView() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.LOGO1);
        Image img = new Image(stream);
        this.imgView.setImage(img);
    }

    private void initializeBackground() {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        BackgroundImage bgImg = ImageUtils.loadBackgroundImage(Resources.Images.BACKGROUND, backgroundSize);
        anchorPane.setBackground(new Background(bgImg));
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

    private void checkFields() {
        String red = "#ef5353";
        String black = "#151928";
        if (txtPassword.getText().isEmpty() || !txtPassword.getText().equals("mdp")) {
            changeNodeColor(txtPassword, red);
            changeLabelVisibility(true);
        }
        if (txtFirstName.getText().isEmpty() || !txtFirstName.getText().equals("Sophie")) {
            changeNodeColor(txtFirstName, red);
            changeLabelVisibility(true);
        }
        if (txtLastName.getText().isEmpty() || !txtLastName.getText().equals("Pages")) {
            changeNodeColor(txtLastName, red);
            changeLabelVisibility(true);
        }
        if (txtMail.getText().isEmpty() || !txtMail.getText().equals("sophie.pages@gmail.com")) {
            changeNodeColor(txtMail, red);
            changeLabelVisibility(true);
        }

        //if bon
        if (txtFirstName.getText().equals("Sophie") && txtPassword.getText().equals("mdp") && txtMail.getText().equals("sophie.pages@gmail.com") && txtLastName.getText().equals("Pages")) {
            changeLabelVisibility(false);
            System.out.println("ON EST SUR LA SCENE SUIVANTE WALLAHs");
            changeNodeColor(txtPassword, black);
            changeNodeColor(txtFirstName, black);
            changeNodeColor(txtLastName, black);
            changeNodeColor(txtMail, black);
        }
        tryingToConnectToServer();
    }

    private void changeLabelVisibility(boolean value) {
        txtLabel.setVisible(value);

    }

    //red #ef5353
    //black #151928
    private void changeNodeColor(JFXTextField txtField, String color) {
        txtField.setFocusColor(Paint.valueOf(color));
        txtField.setUnFocusColor(Paint.valueOf(color));
        txtField.setStyle(String.format("-fx-text-inner-color : %s", color));
        txtField.setStyle(String.format("-fx-prompt-text-fill : %s", color));
        txtField.setStyle(String.format("-fx-text-inner-color: %s", color));
    }

    private void changeNodeColor(JFXPasswordField txtField, String color) {
        txtField.setFocusColor(Paint.valueOf(color));
        txtField.setUnFocusColor(Paint.valueOf(color));
        txtField.setStyle(String.format("-fx-text-inner-color : %s", color));
        txtField.setStyle(String.format("-fx-prompt-text-fill : %s", color));
        txtField.setStyle(String.format("-fx-text-inner-color: %s", color));
    }


    private void tryingToConnectToServer() {
        lbConnectToServer.setVisible(true);
        TranslateTransition translate = new TranslateTransition(Duration.seconds(.4), lbConnectToServer);
        translate.setFromX(0.0);
        translate.setFromY(0);
        translate.setToX(0);
        translate.setToY(-50);
        translate.play();

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(5), lbConnectToServer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
        fadeOut.setOnFinished((ActionEvent event) -> lbConnectToServer.setVisible(false));
        lbConnectToServer.setOnMouseClicked((MouseEvent event) -> {
                    lbConnectToServer.setVisible(false);
                }
        );
    }


    public void checkTime() {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today.minus(Period.ofYears(5))) > 0);
            }
        });
    }
}
